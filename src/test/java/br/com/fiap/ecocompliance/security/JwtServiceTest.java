package br.com.fiap.ecocompliance.security;

import br.com.fiap.ecocompliance.entity.Role;
import br.com.fiap.ecocompliance.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final String SECRET =
            "Y2hhdmUtc2VjcmV0YS1lY29jb21wbGlhbmNlLWFwaS1maWFwLTIwMjUtbXVpdG8tbG9uZ2EtZS1zZWd1cmE=";

    private JwtService jwtService;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", 3_600_000L);
        usuario = Usuario.builder()
                .nome("Administrador")
                .email("admin@ecocompliance.com")
                .senha("hash")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("token gerado deve carregar o e-mail e ser aceito para o mesmo usuário")
    void gerarToken_deveSerValidoParaOMesmoUsuario() {
        String token = jwtService.gerarToken(usuario);

        assertThat(jwtService.extrairUsername(token)).isEqualTo("admin@ecocompliance.com");
        assertThat(jwtService.tokenValido(token, usuario)).isTrue();
    }

    @Test
    @DisplayName("token com claim extra deve expor essa claim")
    void gerarToken_comClaimsExtras_devePermitirExtrairClaim() {
        String token = jwtService.gerarToken(Map.of("origem", "teste"), usuario);

        String origem = jwtService.extrairClaim(token, claims -> claims.get("origem", String.class));

        assertThat(origem).isEqualTo("teste");
        assertThat(jwtService.extrairClaim(token, Claims::getSubject)).isEqualTo(usuario.getUsername());
    }

    @Test
    @DisplayName("token de outro usuário deve ser recusado")
    void tokenValido_quandoUsernameDiferente_deveSerFalso() {
        String token = jwtService.gerarToken(usuario);
        UserDetails outro = Usuario.builder()
                .nome("Outro")
                .email("outro@ecocompliance.com")
                .senha("hash")
                .role(Role.USER)
                .build();

        assertThat(jwtService.tokenValido(token, outro)).isFalse();
    }

    @Test
    @DisplayName("token expirado deve ser recusado na leitura")
    void tokenValido_quandoExpirado_deveLancarExpiredJwt() {
        ReflectionTestUtils.setField(jwtService, "expiration", -60_000L);
        String token = jwtService.gerarToken(usuario);

        assertThatThrownBy(() -> jwtService.tokenValido(token, usuario))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
