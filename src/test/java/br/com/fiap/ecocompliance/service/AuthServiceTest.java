package br.com.fiap.ecocompliance.service;

import br.com.fiap.ecocompliance.dto.AuthResponseDTO;
import br.com.fiap.ecocompliance.dto.LoginRequestDTO;
import br.com.fiap.ecocompliance.dto.RegisterRequestDTO;
import br.com.fiap.ecocompliance.entity.Role;
import br.com.fiap.ecocompliance.entity.Usuario;
import br.com.fiap.ecocompliance.exception.EmailAlreadyExistsException;
import br.com.fiap.ecocompliance.repository.UsuarioRepository;
import br.com.fiap.ecocompliance.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register deve gravar usuário com senha criptografada e devolver token")
    void register_quandoEmailLivre_deveSalvarEDevolverToken() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Novo Admin", "novo@ecocompliance.com", "admin123", Role.ADMIN);
        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.senha())).thenReturn("hash");
        when(jwtService.gerarToken(any(UserDetails.class))).thenReturn("token-jwt");

        AuthResponseDTO resposta = authService.register(dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario salvo = captor.getValue();
        assertThat(salvo.getNome()).isEqualTo("Novo Admin");
        assertThat(salvo.getEmail()).isEqualTo("novo@ecocompliance.com");
        assertThat(salvo.getSenha()).isEqualTo("hash");
        assertThat(salvo.getRole()).isEqualTo(Role.ADMIN);
        assertThat(resposta.token()).isEqualTo("token-jwt");
        assertThat(resposta.tipo()).isEqualTo("Bearer");
        assertThat(resposta.email()).isEqualTo("novo@ecocompliance.com");
        assertThat(resposta.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("register deve rejeitar e-mail já cadastrado")
    void register_quandoEmailExiste_deveLancarConflito() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Ana", "ana@ecocompliance.com", "senha123", Role.USER);
        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("ana@ecocompliance.com");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("login deve autenticar e devolver token do usuário encontrado")
    void login_quandoCredenciaisValidas_deveDevolverToken() {
        LoginRequestDTO dto = new LoginRequestDTO("admin@ecocompliance.com", "admin123");
        Usuario usuario = Usuario.builder()
                .nome("Administrador")
                .email(dto.email())
                .senha("hash")
                .role(Role.ADMIN)
                .build();
        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.of(usuario));
        when(jwtService.gerarToken(usuario)).thenReturn("token-login");

        AuthResponseDTO resposta = authService.login(dto);

        verify(authenticationManager).authenticate(any());
        assertThat(resposta.token()).isEqualTo("token-login");
        assertThat(resposta.email()).isEqualTo(dto.email());
        assertThat(resposta.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("login deve propagar credencial inválida")
    void login_quandoSenhaIncorreta_devePropagarBadCredentials() {
        LoginRequestDTO dto = new LoginRequestDTO("admin@ecocompliance.com", "errada");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(BadCredentialsException.class);
        verify(usuarioRepository, never()).findByEmail(any());
    }

    @Test
    @DisplayName("login deve falhar quando o usuário some depois da autenticação")
    void login_quandoUsuarioNaoEncontrado_deveLancarRuntimeException() {
        LoginRequestDTO dto = new LoginRequestDTO("sumiu@ecocompliance.com", "senha123");
        when(usuarioRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuário não encontrado");
    }
}
