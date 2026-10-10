package br.com.fiap.ecocompliance.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioTest {

    @Test
    @DisplayName("UserDetails deve usar e-mail, senha e role com prefixo ROLE_")
    void userDetails_deveExporCredenciaisERole() {
        Usuario usuario = Usuario.builder()
                .nome("Administrador")
                .email("admin@ecocompliance.com")
                .senha("hash")
                .role(Role.ADMIN)
                .build();

        assertThat(usuario.getUsername()).isEqualTo("admin@ecocompliance.com");
        assertThat(usuario.getPassword()).isEqualTo("hash");
        assertThat(usuario.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
        assertThat(usuario.isAccountNonExpired()).isTrue();
        assertThat(usuario.isAccountNonLocked()).isTrue();
        assertThat(usuario.isCredentialsNonExpired()).isTrue();
        assertThat(usuario.isEnabled()).isTrue();
    }
}
