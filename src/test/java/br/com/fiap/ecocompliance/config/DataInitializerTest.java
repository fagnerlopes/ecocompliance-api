package br.com.fiap.ecocompliance.config;

import br.com.fiap.ecocompliance.entity.Role;
import br.com.fiap.ecocompliance.entity.Usuario;
import br.com.fiap.ecocompliance.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    @DisplayName("deve criar admin e user quando os e-mails ainda não existem")
    void run_quandoUsuariosNaoExistem_deveCriarOsDois() {
        when(usuarioRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("hash-admin");
        when(passwordEncoder.encode("user123")).thenReturn("hash-user");

        dataInitializer.run();

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(2)).save(captor.capture());
        List<Usuario> salvos = captor.getAllValues();
        assertThat(salvos).extracting(Usuario::getEmail)
                .containsExactly("admin@ecocompliance.com", "user@ecocompliance.com");
        assertThat(salvos).extracting(Usuario::getRole)
                .containsExactly(Role.ADMIN, Role.USER);
        assertThat(salvos).extracting(Usuario::getSenha)
                .containsExactly("hash-admin", "hash-user");
    }

    @Test
    @DisplayName("não deve gravar quando os dois usuários já existem")
    void run_quandoUsuariosJaExistem_naoSalva() {
        when(usuarioRepository.existsByEmail(any())).thenReturn(true);

        dataInitializer.run();

        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("deve criar só o usuário que ainda não existe")
    void run_quandoSoOUserFalta_criaApenasOUser() {
        when(usuarioRepository.existsByEmail("admin@ecocompliance.com")).thenReturn(true);
        when(usuarioRepository.existsByEmail("user@ecocompliance.com")).thenReturn(false);
        when(passwordEncoder.encode("user123")).thenReturn("hash-user");

        dataInitializer.run();

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("user@ecocompliance.com");
        assertThat(captor.getValue().getRole()).isEqualTo(Role.USER);
    }
}
