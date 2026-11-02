package br.com.fiap.ecocompliance.bdd;

import br.com.fiap.ecocompliance.contrato.Amostras;
import br.com.fiap.ecocompliance.contrato.ValidadorJsonSchema;
import br.com.fiap.ecocompliance.exception.ResourceNotFoundException;
import br.com.fiap.ecocompliance.service.AuthService;
import br.com.fiap.ecocompliance.service.EmpresaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class PassosGovernanca {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    AuthService authService;

    private final ValidadorJsonSchema validador = new ValidadorJsonSchema();
    private String papel = "ADMIN";
    private MvcResult resultado;

    @Before
    public void limpar() {
        reset(empresaService, authService);
        papel = "ADMIN";
    }

    @Dado("um administrador autenticado")
    public void administrador() {
        papel = "ADMIN";
    }

    @Dado("um usuário autenticado")
    public void usuario() {
        papel = "USER";
    }

    @Quando("ele cadastra a empresa {string} com CNPJ {string}")
    public void cadastraEmpresa(String nome, String cnpj) throws Exception {
        when(empresaService.criar(any())).thenReturn(new br.com.fiap.ecocompliance.dto.EmpresaResponseDTO(
                1L, nome, cnpj, "Mineração", "contato@verdenorte.com.br", Amostras.CRIADO_EM));
        String corpo = """
                {"nome":"%s","cnpj":"%s","setor":"Mineração","emailResponsavel":"contato@verdenorte.com.br"}
                """.formatted(nome, cnpj);
        resultado = mockMvc.perform(post("/empresas")
                        .with(user("qa").roles(papel))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andReturn();
    }

    @Quando("ele envia um cadastro de empresa sem nome")
    public void empresaSemNome() throws Exception {
        String corpo = """
                {"nome":"","cnpj":"12.345.678/0001-90","setor":"Mineração","emailResponsavel":"contato@verdenorte.com.br"}
                """;
        resultado = mockMvc.perform(post("/empresas")
                        .with(user("qa").roles(papel))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andReturn();
    }

    @Quando("ele busca a empresa de id {int}")
    public void buscaEmpresa(int id) throws Exception {
        when(empresaService.buscarPorId((long) id))
                .thenThrow(new ResourceNotFoundException("Empresa não encontrada"));
        resultado = mockMvc.perform(get("/empresas/" + id).with(user("qa").roles(papel)))
                .andReturn();
    }

    @Quando("o usuário entra com o e-mail {string} e a senha {string}")
    public void login(String email, String senha) throws Exception {
        when(authService.login(any())).thenReturn(Amostras.auth());
        String corpo = """
                {"email":"%s","senha":"%s"}
                """.formatted(email, senha);
        resultado = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andReturn();
    }

    @Então("a API responde {int}")
    public void status(int esperado) {
        assertEquals(esperado, resultado.getResponse().getStatus());
    }

    @E("o corpo contém o nome {string}")
    public void corpoContemNome(String nome) throws Exception {
        assertEquals(nome, json().get("nome").asText());
    }

    @E("o contrato JSON da empresa é respeitado")
    public void contratoEmpresa() throws Exception {
        validador.validarObjeto(corpo(), "empresa.json");
    }

    @E("o corpo informa erro de validação")
    public void erroDeValidacao() throws Exception {
        JsonNode no = json();
        validador.validarObjeto(corpo(), "erro.json");
        assertEquals(400, no.get("status").asInt());
        assertEquals("Erro de validação nos campos", no.get("message").asText());
        assertFalse(no.get("fieldErrors").isEmpty());
        assertEquals("nome", no.get("fieldErrors").get(0).get("campo").asText());
    }

    @E("a mensagem informa que o recurso não foi encontrado")
    public void recursoNaoEncontrado() throws Exception {
        JsonNode no = json();
        validador.validarObjeto(corpo(), "erro.json");
        assertEquals(404, no.get("status").asInt());
        assertTrue(no.get("message").asText().contains("não encontrada"));
    }

    @E("o corpo contém um token Bearer")
    public void tokenBearer() throws Exception {
        JsonNode no = json();
        validador.validarObjeto(corpo(), "auth.json");
        assertEquals("Bearer", no.get("tipo").asText());
        assertFalse(no.get("token").asText().isBlank());
    }

    private String corpo() throws Exception {
        return resultado.getResponse().getContentAsString(java.nio.charset.StandardCharsets.UTF_8);
    }

    private JsonNode json() throws Exception {
        return objectMapper.readTree(corpo());
    }
}
