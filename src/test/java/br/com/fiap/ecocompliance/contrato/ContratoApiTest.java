package br.com.fiap.ecocompliance.contrato;

import br.com.fiap.ecocompliance.config.SecurityConfig;
import br.com.fiap.ecocompliance.controller.AgendamentoController;
import br.com.fiap.ecocompliance.controller.AmbienteController;
import br.com.fiap.ecocompliance.controller.AuditoriaController;
import br.com.fiap.ecocompliance.controller.AuthController;
import br.com.fiap.ecocompliance.controller.CompensacaoController;
import br.com.fiap.ecocompliance.controller.EmissaoController;
import br.com.fiap.ecocompliance.controller.EmpresaController;
import br.com.fiap.ecocompliance.controller.IndicadoresController;
import br.com.fiap.ecocompliance.controller.LicencaController;
import br.com.fiap.ecocompliance.controller.RelatorioController;
import br.com.fiap.ecocompliance.controller.UpController;
import br.com.fiap.ecocompliance.entity.NivelRisco;
import br.com.fiap.ecocompliance.entity.StatusAgendamento;
import br.com.fiap.ecocompliance.exception.ResourceNotFoundException;
import br.com.fiap.ecocompliance.repository.UsuarioRepository;
import br.com.fiap.ecocompliance.security.JwtService;
import br.com.fiap.ecocompliance.service.AgendamentoService;
import br.com.fiap.ecocompliance.service.AuditoriaService;
import br.com.fiap.ecocompliance.service.AuthService;
import br.com.fiap.ecocompliance.service.CompensacaoService;
import br.com.fiap.ecocompliance.service.EmissaoService;
import br.com.fiap.ecocompliance.service.EmpresaService;
import br.com.fiap.ecocompliance.service.IndicadoresService;
import br.com.fiap.ecocompliance.service.LicencaService;
import br.com.fiap.ecocompliance.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        EmpresaController.class,
        LicencaController.class,
        AuditoriaController.class,
        EmissaoController.class,
        CompensacaoController.class,
        AgendamentoController.class,
        AuthController.class,
        AmbienteController.class,
        UpController.class,
        IndicadoresController.class,
        RelatorioController.class
})
@Import(SecurityConfig.class)
@WithMockUser(username = "admin@ecocompliance.com", roles = "ADMIN")
class ContratoApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmpresaService empresaService;

    @MockBean
    LicencaService licencaService;

    @MockBean
    AuditoriaService auditoriaService;

    @MockBean
    EmissaoService emissaoService;

    @MockBean
    CompensacaoService compensacaoService;

    @MockBean
    AgendamentoService agendamentoService;

    @MockBean
    AuthService authService;

    @MockBean
    IndicadoresService indicadoresService;

    @MockBean
    RelatorioService relatorioService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UsuarioRepository usuarioRepository;

    private final ValidadorJsonSchema validador = new ValidadorJsonSchema();

    @BeforeEach
    void preparar() {
        lenient().when(empresaService.listar(any())).thenReturn(pagina(List.of(Amostras.empresa())));
        lenient().when(empresaService.buscarPorId(1L)).thenReturn(Amostras.empresa());
        lenient().when(empresaService.criar(any())).thenReturn(Amostras.empresa());
        lenient().when(empresaService.atualizar(anyLong(), any())).thenReturn(Amostras.empresa());

        lenient().when(licencaService.listar(any())).thenReturn(pagina(List.of(Amostras.licenca())));
        lenient().when(licencaService.buscarPorId(10L)).thenReturn(Amostras.licenca());
        lenient().when(licencaService.licencasVencendo()).thenReturn(List.of(Amostras.licenca()));
        lenient().when(licencaService.criar(any())).thenReturn(Amostras.licenca());
        lenient().when(licencaService.atualizar(anyLong(), any())).thenReturn(Amostras.licenca());

        lenient().when(auditoriaService.listar(any())).thenReturn(pagina(List.of(Amostras.auditoria())));
        lenient().when(auditoriaService.buscarPorId(4L)).thenReturn(Amostras.auditoria());
        lenient().when(auditoriaService.buscarPorRisco(NivelRisco.ALTO)).thenReturn(List.of(Amostras.auditoria()));
        lenient().when(auditoriaService.criar(any())).thenReturn(Amostras.auditoria());
        lenient().when(auditoriaService.atualizar(anyLong(), any())).thenReturn(Amostras.auditoria());

        lenient().when(emissaoService.listar(any())).thenReturn(pagina(List.of(Amostras.emissao())));
        lenient().when(emissaoService.buscarPorId(7L)).thenReturn(Amostras.emissao());
        lenient().when(emissaoService.buscarPorEmpresa(1L)).thenReturn(List.of(Amostras.emissao()));
        lenient().when(emissaoService.criar(any())).thenReturn(Amostras.emissao());
        lenient().when(emissaoService.atualizar(anyLong(), any())).thenReturn(Amostras.emissao());

        lenient().when(compensacaoService.listar(any())).thenReturn(pagina(List.of(Amostras.compensacao())));
        lenient().when(compensacaoService.buscarPorId(3L)).thenReturn(Amostras.compensacao());
        lenient().when(compensacaoService.criar(any())).thenReturn(Amostras.compensacao());
        lenient().when(compensacaoService.atualizar(anyLong(), any())).thenReturn(Amostras.compensacao());

        lenient().when(agendamentoService.listar(any())).thenReturn(pagina(List.of(Amostras.agendamento())));
        lenient().when(agendamentoService.buscarPorId(2L)).thenReturn(Amostras.agendamento());
        lenient().when(agendamentoService.buscarPorEmpresa(1L)).thenReturn(List.of(Amostras.agendamento()));
        lenient().when(agendamentoService.buscarPorStatus(StatusAgendamento.PENDENTE)).thenReturn(List.of(Amostras.agendamento()));
        lenient().when(agendamentoService.criar(any())).thenReturn(Amostras.agendamento());
        lenient().when(agendamentoService.atualizar(anyLong(), any())).thenReturn(Amostras.agendamento());

        lenient().when(authService.register(any())).thenReturn(Amostras.auth());
        lenient().when(authService.login(any())).thenReturn(Amostras.auth());
        lenient().when(indicadoresService.calcular()).thenReturn(Amostras.indicadores());
        lenient().when(relatorioService.gerarRelatorioEmpresa(1L)).thenReturn(Amostras.relatorio());
    }

    @Test
    @DisplayName("GET /empresas devolve 200, a página e o contrato da empresa")
    void listarEmpresas() throws Exception {
        pagina("/empresas", "empresa.json", "$.content[0].nome", Amostras.EMPRESA);
    }

    @Test
    @DisplayName("GET /empresas/{id} devolve 200 e o contrato da empresa")
    void buscarEmpresa() throws Exception {
        objeto("/empresas/1", "empresa.json", "$.cnpj", "12.345.678/0001-90");
    }

    @Test
    @DisplayName("POST /empresas devolve 201, Location e o contrato da empresa")
    void criarEmpresa() throws Exception {
        criar("/empresas", Amostras.empresaJson(), "empresa.json", "$.nome", Amostras.EMPRESA);
    }

    @Test
    @DisplayName("PUT /empresas/{id} devolve 200 e o contrato da empresa")
    void atualizarEmpresa() throws Exception {
        atualizar("/empresas/1", Amostras.empresaJson(), "empresa.json", "$.setor", "Mineração");
    }

    @Test
    @DisplayName("DELETE /empresas/{id} devolve 204 sem corpo")
    void excluirEmpresa() throws Exception {
        excluir("/empresas/1");
    }

    @Test
    @DisplayName("POST /empresas sem nome devolve 400 no contrato de erro")
    void empresaInvalida() throws Exception {
        String corpo = mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Erro de validação nos campos"))
                .andExpect(jsonPath("$.fieldErrors[0].campo").exists())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, "erro.json");
    }

    @Test
    @DisplayName("GET /empresas/{id} inexistente devolve 404 no contrato de erro")
    void empresaInexistente() throws Exception {
        when(empresaService.buscarPorId(999L)).thenThrow(new ResourceNotFoundException("Empresa não encontrada"));
        String corpo = mockMvc.perform(get("/empresas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Empresa não encontrada"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, "erro.json");
    }

    @Test
    @DisplayName("GET /licencas devolve 200, a página e o contrato da licença")
    void listarLicencas() throws Exception {
        pagina("/licencas", "licenca.json", "$.content[0].status", "ATIVA");
    }

    @Test
    @DisplayName("GET /licencas/{id} devolve 200 e o contrato da licença")
    void buscarLicenca() throws Exception {
        objeto("/licencas/10", "licenca.json", "$.numeroLicenca", "LO-2026-0042");
    }

    @Test
    @DisplayName("GET /licencas/vencendo devolve 200 e a lista no contrato da licença")
    void licencasVencendo() throws Exception {
        lista("/licencas/vencendo", "licenca.json", "$[0].orgaoEmissor", "CETESB");
    }

    @Test
    @DisplayName("POST /licencas devolve 201, Location e o contrato da licença")
    void criarLicenca() throws Exception {
        criar("/licencas", Amostras.licencaJson(), "licenca.json", "$.status", "ATIVA");
    }

    @Test
    @DisplayName("PUT /licencas/{id} devolve 200 e o contrato da licença")
    void atualizarLicenca() throws Exception {
        atualizar("/licencas/10", Amostras.licencaJson(), "licenca.json", "$.tipoLicenca", "Licença de Operação");
    }

    @Test
    @DisplayName("DELETE /licencas/{id} devolve 204 sem corpo")
    void excluirLicenca() throws Exception {
        excluir("/licencas/10");
    }

    @Test
    @DisplayName("GET /auditorias devolve 200, a página e o contrato da auditoria")
    void listarAuditorias() throws Exception {
        pagina("/auditorias", "auditoria.json", "$.content[0].nivelRisco", "ALTO");
    }

    @Test
    @DisplayName("GET /auditorias/{id} devolve 200 e o contrato da auditoria")
    void buscarAuditoria() throws Exception {
        objeto("/auditorias/4", "auditoria.json", "$.nivelRisco", "ALTO");
    }

    @Test
    @DisplayName("GET /auditorias/risco/{nivel} devolve 200 e a lista no contrato da auditoria")
    void auditoriasPorRisco() throws Exception {
        lista("/auditorias/risco/ALTO", "auditoria.json", "$[0].nivelRisco", "ALTO");
    }

    @Test
    @DisplayName("POST /auditorias devolve 201, Location e o contrato da auditoria")
    void criarAuditoria() throws Exception {
        criar("/auditorias", Amostras.auditoriaJson(), "auditoria.json", "$.nivelRisco", "ALTO");
    }

    @Test
    @DisplayName("PUT /auditorias/{id} devolve 200 e o contrato da auditoria")
    void atualizarAuditoria() throws Exception {
        atualizar("/auditorias/4", Amostras.auditoriaJson(), "auditoria.json", "$.nivelRisco", "ALTO");
    }

    @Test
    @DisplayName("DELETE /auditorias/{id} devolve 204 sem corpo")
    void excluirAuditoria() throws Exception {
        excluir("/auditorias/4");
    }

    @Test
    @DisplayName("GET /emissoes devolve 200, a página e o contrato da emissão")
    void listarEmissoes() throws Exception {
        pagina("/emissoes", "emissao.json", "$.content[0].unidadeMedida", "tCO2e");
    }

    @Test
    @DisplayName("GET /emissoes/{id} devolve 200 e o contrato da emissão")
    void buscarEmissao() throws Exception {
        objeto("/emissoes/7", "emissao.json", "$.fonteEmissao", "Caldeira a gás natural");
    }

    @Test
    @DisplayName("GET /emissoes/empresa/{id} devolve 200 e a lista no contrato da emissão")
    void emissoesPorEmpresa() throws Exception {
        lista("/emissoes/empresa/1", "emissao.json", "$[0].unidadeMedida", "tCO2e");
    }

    @Test
    @DisplayName("POST /emissoes devolve 201, Location e o contrato da emissão")
    void criarEmissao() throws Exception {
        criar("/emissoes", Amostras.emissaoJson(), "emissao.json", "$.unidadeMedida", "tCO2e");
    }

    @Test
    @DisplayName("PUT /emissoes/{id} devolve 200 e o contrato da emissão")
    void atualizarEmissao() throws Exception {
        atualizar("/emissoes/7", Amostras.emissaoJson(), "emissao.json", "$.fonteEmissao", "Caldeira a gás natural");
    }

    @Test
    @DisplayName("DELETE /emissoes/{id} devolve 204 sem corpo")
    void excluirEmissao() throws Exception {
        excluir("/emissoes/7");
    }

    @Test
    @DisplayName("GET /compensacoes devolve 200, a página e o contrato da compensação")
    void listarCompensacoes() throws Exception {
        pagina("/compensacoes", "compensacao.json", "$.content[0].tipoCompensacao", "Reflorestamento");
    }

    @Test
    @DisplayName("GET /compensacoes/{id} devolve 200 e o contrato da compensação")
    void buscarCompensacao() throws Exception {
        objeto("/compensacoes/3", "compensacao.json", "$.tipoCompensacao", "Reflorestamento");
    }

    @Test
    @DisplayName("POST /compensacoes devolve 201, Location e o contrato da compensação")
    void criarCompensacao() throws Exception {
        criar("/compensacoes", Amostras.compensacaoJson(), "compensacao.json", "$.tipoCompensacao", "Reflorestamento");
    }

    @Test
    @DisplayName("PUT /compensacoes/{id} devolve 200 e o contrato da compensação")
    void atualizarCompensacao() throws Exception {
        atualizar("/compensacoes/3", Amostras.compensacaoJson(), "compensacao.json", "$.tipoCompensacao", "Reflorestamento");
    }

    @Test
    @DisplayName("DELETE /compensacoes/{id} devolve 204 sem corpo")
    void excluirCompensacao() throws Exception {
        excluir("/compensacoes/3");
    }

    @Test
    @DisplayName("GET /agendamentos-reducao devolve 200, a página e o contrato do agendamento")
    void listarAgendamentos() throws Exception {
        pagina("/agendamentos-reducao", "agendamento.json", "$.content[0].status", "PENDENTE");
    }

    @Test
    @DisplayName("GET /agendamentos-reducao/{id} devolve 200 e o contrato do agendamento")
    void buscarAgendamento() throws Exception {
        objeto("/agendamentos-reducao/2", "agendamento.json", "$.titulo", "Troca da frota a diesel");
    }

    @Test
    @DisplayName("GET /agendamentos-reducao/empresa/{id} devolve 200 e a lista no contrato do agendamento")
    void agendamentosPorEmpresa() throws Exception {
        lista("/agendamentos-reducao/empresa/1", "agendamento.json", "$[0].status", "PENDENTE");
    }

    @Test
    @DisplayName("GET /agendamentos-reducao/status/{status} devolve 200 e a lista no contrato do agendamento")
    void agendamentosPorStatus() throws Exception {
        lista("/agendamentos-reducao/status/PENDENTE", "agendamento.json", "$[0].status", "PENDENTE");
    }

    @Test
    @DisplayName("POST /agendamentos-reducao devolve 201, Location e o contrato do agendamento")
    void criarAgendamento() throws Exception {
        criar("/agendamentos-reducao", Amostras.agendamentoJson(), "agendamento.json", "$.status", "PENDENTE");
    }

    @Test
    @DisplayName("PUT /agendamentos-reducao/{id} devolve 200 e o contrato do agendamento")
    void atualizarAgendamento() throws Exception {
        atualizar("/agendamentos-reducao/2", Amostras.agendamentoJson(), "agendamento.json", "$.titulo", "Troca da frota a diesel");
    }

    @Test
    @DisplayName("DELETE /agendamentos-reducao/{id} devolve 204 sem corpo")
    void excluirAgendamento() throws Exception {
        excluir("/agendamentos-reducao/2");
    }

    @Test
    @DisplayName("POST /auth/register devolve 201 e o contrato do token")
    void registrar() throws Exception {
        String corpo = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Amostras.registerJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, "auth.json");
    }

    @Test
    @DisplayName("POST /auth/login devolve 200 e o contrato do token")
    void entrar() throws Exception {
        String corpo = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Amostras.loginJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@ecocompliance.com"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, "auth.json");
    }

    @Test
    @DisplayName("POST /auth/login sem senha devolve 400 no contrato de erro")
    void loginInvalido() throws Exception {
        String corpo = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@ecocompliance.com\",\"senha\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].campo").value("senha"))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, "erro.json");
    }

    @Test
    @DisplayName("GET /relatorios/empresa/{id} devolve 200 e o contrato do relatório")
    void relatorio() throws Exception {
        objeto("/relatorios/empresa/1", "relatorio.json", "$.maiorNivelRisco", "ALTO");
    }

    @Test
    @DisplayName("GET /indicadores-sustentabilidade devolve 200 e o contrato dos indicadores")
    void indicadores() throws Exception {
        objeto("/indicadores-sustentabilidade", "indicadores.json", "$.totalEmpresas", 4);
    }

    @Test
    @DisplayName("GET /ambiente devolve 200 e o contrato do ambiente")
    void ambiente() throws Exception {
        objeto("/ambiente", "ambiente.json", "$.aplicacao", "ecocompliance");
    }

    @Test
    @DisplayName("GET /up devolve 200 e o contrato de saúde")
    void up() throws Exception {
        objeto("/up", "up.json", "$.status", "up");
    }

    private static <T> PageImpl<T> pagina(List<T> itens) {
        return new PageImpl<>(itens, PageRequest.of(0, 20), itens.size());
    }

    private void pagina(String url, String schema, String caminho, String valor) throws Exception {
        String corpo = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(caminho).value(valor))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarPagina(corpo, schema);
    }

    private void lista(String url, String schema, String caminho, String valor) throws Exception {
        String corpo = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(caminho).value(valor))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarLista(corpo, schema);
    }

    private void objeto(String url, String schema, String caminho, Object valor) throws Exception {
        String corpo = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(caminho).value(valor))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, schema);
    }

    private void criar(String url, String json, String schema, String caminho, String valor) throws Exception {
        String corpo = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(url + "/")))
                .andExpect(jsonPath(caminho).value(valor))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, schema);
    }

    private void atualizar(String url, String json, String schema, String caminho, String valor) throws Exception {
        String corpo = mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath(caminho).value(valor))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        validador.validarObjeto(corpo, schema);
    }

    private void excluir(String url) throws Exception {
        mockMvc.perform(delete(url))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
