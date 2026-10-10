package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.IndicadoresSustentabilidadeDTO;
import br.com.fiap.ecocompliance.service.IndicadoresService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IndicadoresController.class)
@Import(br.com.fiap.ecocompliance.config.SecurityConfig.class)
class IndicadoresControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IndicadoresService indicadoresService;

    @MockBean
    br.com.fiap.ecocompliance.security.JwtService jwtService;

    @MockBean
    br.com.fiap.ecocompliance.repository.UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("indicadores com usuário autenticado deve retornar 200")
    @WithMockUser(roles = "USER")
    void indicadores_comUser_retorna200() throws Exception {
        when(indicadoresService.calcular()).thenReturn(new IndicadoresSustentabilidadeDTO(
                3,
                new BigDecimal("7051.5"),
                new BigDecimal("4200"),
                new BigDecimal("-2851.5"),
                4,
                1,
                3,
                1,
                1,
                2));

        mockMvc.perform(get("/indicadores-sustentabilidade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEmpresas").value(3))
                .andExpect(jsonPath("$.saldoLiquidoCarbono").value(-2851.5))
                .andExpect(jsonPath("$.agendamentosPendentes").value(2));
    }

    @Test
    @DisplayName("indicadores sem autenticação deve retornar 403")
    void indicadores_semAutenticacao_retorna403() throws Exception {
        mockMvc.perform(get("/indicadores-sustentabilidade"))
                .andExpect(status().isForbidden());
    }
}
