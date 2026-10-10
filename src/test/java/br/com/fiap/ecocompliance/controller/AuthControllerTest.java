package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.AuthResponseDTO;
import br.com.fiap.ecocompliance.entity.Role;
import br.com.fiap.ecocompliance.exception.EmailAlreadyExistsException;
import br.com.fiap.ecocompliance.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(br.com.fiap.ecocompliance.config.SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    br.com.fiap.ecocompliance.security.JwtService jwtService;

    @MockBean
    br.com.fiap.ecocompliance.repository.UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("register válido deve retornar 201 com token")
    void register_valido_retorna201() throws Exception {
        when(authService.register(any())).thenReturn(
                AuthResponseDTO.of("token-novo", "novo@ecocompliance.com", "Novo", Role.USER));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "nome", "Novo",
                                "email", "novo@ecocompliance.com",
                                "senha", "senha123",
                                "role", "USER"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token-novo"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("register com e-mail duplicado deve retornar 409")
    void register_emailDuplicado_retorna409() throws Exception {
        when(authService.register(any()))
                .thenThrow(new EmailAlreadyExistsException("E-mail já cadastrado: novo@ecocompliance.com"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "nome", "Novo",
                                "email", "novo@ecocompliance.com",
                                "senha", "senha123",
                                "role", "USER"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("E-mail já cadastrado: novo@ecocompliance.com"));
    }

    @Test
    @DisplayName("register inválido deve retornar 400 com erros de campo")
    void register_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    @DisplayName("login válido deve retornar 200 com token")
    void login_valido_retorna200() throws Exception {
        when(authService.login(any())).thenReturn(
                AuthResponseDTO.of("token-login", "admin@ecocompliance.com", "Admin", Role.ADMIN));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "admin@ecocompliance.com",
                                "senha", "admin123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@ecocompliance.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("login com senha incorreta deve retornar 401")
    void login_senhaIncorreta_retorna401() throws Exception {
        when(authService.login(any())).thenThrow(new BadCredentialsException("bad"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "admin@ecocompliance.com",
                                "senha", "errada"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
    }
}
