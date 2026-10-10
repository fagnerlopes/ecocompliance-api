package br.com.fiap.ecocompliance.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AmbienteController.class, UpController.class})
@Import(br.com.fiap.ecocompliance.config.SecurityConfig.class)
@TestPropertySource(properties = "ecocompliance.ambiente=staging")
class AmbienteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    br.com.fiap.ecocompliance.security.JwtService jwtService;

    @MockBean
    br.com.fiap.ecocompliance.repository.UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("ambiente é público e informa o perfil configurado")
    void ambiente_semAutenticacao_retorna200() throws Exception {
        mockMvc.perform(get("/ambiente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aplicacao").value("ecocompliance"))
                .andExpect(jsonPath("$.ambiente").value("staging"));
    }

    @Test
    @DisplayName("healthcheck público para o proxy da nuvem")
    void up_semAutenticacao_retorna200() throws Exception {
        mockMvc.perform(get("/up"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("up"));
    }
}
