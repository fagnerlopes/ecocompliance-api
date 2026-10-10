package br.com.fiap.ecocompliance.security;

import br.com.fiap.ecocompliance.entity.Role;
import br.com.fiap.ecocompliance.entity.Usuario;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        usuario = Usuario.builder()
                .nome("Administrador")
                .email("admin@ecocompliance.com")
                .senha("hash")
                .role(Role.ADMIN)
                .build();
    }

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("requisição sem Authorization deve seguir a cadeia sem autenticar")
    void doFilter_semHeader_naoAutentica() throws Exception {
        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extrairUsername(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("header que não é Bearer deve seguir a cadeia sem autenticar")
    void doFilter_headerNaoBearer_naoAutentica() throws Exception {
        request.addHeader("Authorization", "Basic abc");

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer válido deve colocar o usuário no contexto de segurança")
    void doFilter_tokenValido_autentica() throws Exception {
        request.addHeader("Authorization", "Bearer token-ok");
        when(jwtService.extrairUsername("token-ok")).thenReturn(usuario.getEmail());
        when(applicationContext.getBean(UserDetailsService.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(usuario.getEmail())).thenReturn(usuario);
        when(jwtService.tokenValido("token-ok", usuario)).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo(usuario.getEmail());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer inválido não deve autenticar e deve seguir a cadeia")
    void doFilter_tokenInvalido_naoAutentica() throws Exception {
        request.addHeader("Authorization", "Bearer token-ruim");
        when(jwtService.extrairUsername("token-ruim")).thenReturn(usuario.getEmail());
        when(applicationContext.getBean(UserDetailsService.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(usuario.getEmail())).thenReturn(usuario);
        when(jwtService.tokenValido("token-ruim", usuario)).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("token sem subject não deve carregar o usuário")
    void doFilter_semSubject_naoCarregaUsuario() throws Exception {
        request.addHeader("Authorization", "Bearer sem-subject");
        when(jwtService.extrairUsername("sem-subject")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        verify(applicationContext, never()).getBean(UserDetailsService.class);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("usuário já autenticado não deve ser recarregado")
    void doFilter_jaAutenticado_naoRecarregaUsuario() throws Exception {
        request.addHeader("Authorization", "Bearer token-ok");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ja-logado", null, List.of()));
        when(jwtService.extrairUsername("token-ok")).thenReturn(usuario.getEmail());

        filter.doFilter(request, response, filterChain);

        verify(applicationContext, never()).getBean(UserDetailsService.class);
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("ja-logado");
    }

    @Test
    @DisplayName("falha ao ler o JWT deve seguir a cadeia sem autenticar")
    void doFilter_quandoJwtLancaExcecao_segueSemAutenticar() throws Exception {
        request.addHeader("Authorization", "Bearer quebrado");
        when(jwtService.extrairUsername("quebrado")).thenThrow(new IllegalArgumentException("assinatura"));

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
