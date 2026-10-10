package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.EmpresaResponseDTO;
import br.com.fiap.ecocompliance.dto.RelatorioEmpresaDTO;
import br.com.fiap.ecocompliance.entity.NivelRisco;
import br.com.fiap.ecocompliance.exception.ResourceNotFoundException;
import br.com.fiap.ecocompliance.service.RelatorioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RelatorioController.class)
@Import(br.com.fiap.ecocompliance.config.SecurityConfig.class)
class RelatorioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RelatorioService relatorioService;

    @MockBean
    br.com.fiap.ecocompliance.security.JwtService jwtService;

    @MockBean
    br.com.fiap.ecocompliance.repository.UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("relatório com usuário autenticado deve retornar 200")
    @WithMockUser(roles = "USER")
    void relatorio_comUser_retorna200() throws Exception {
        EmpresaResponseDTO empresa = new EmpresaResponseDTO(
                1L, "Verde Industria S.A.", "12.345.678/0001-90",
                "Manufatura", "esg@verde.com.br", LocalDateTime.now());
        when(relatorioService.gerarRelatorioEmpresa(1L)).thenReturn(new RelatorioEmpresaDTO(
                empresa,
                new BigDecimal("2231.25"),
                new BigDecimal("1500"),
                2L,
                0L,
                1L,
                NivelRisco.MEDIO));

        mockMvc.perform(get("/relatorios/empresa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresa.nome").value("Verde Industria S.A."))
                .andExpect(jsonPath("$.totalEmissoesCarbono").value(2231.25))
                .andExpect(jsonPath("$.maiorNivelRisco").value("MEDIO"));
    }

    @Test
    @DisplayName("relatório de empresa inexistente deve retornar 404")
    @WithMockUser(roles = "ADMIN")
    void relatorio_empresaInexistente_retorna404() throws Exception {
        when(relatorioService.gerarRelatorioEmpresa(99L))
                .thenThrow(new ResourceNotFoundException("Empresa não encontrada com ID: 99"));

        mockMvc.perform(get("/relatorios/empresa/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("relatório sem autenticação deve retornar 403")
    void relatorio_semAutenticacao_retorna403() throws Exception {
        mockMvc.perform(get("/relatorios/empresa/1"))
                .andExpect(status().isForbidden());
    }
}
