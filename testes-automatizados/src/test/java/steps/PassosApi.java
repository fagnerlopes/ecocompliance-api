package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.path.json.JsonPath;
import model.Contexto;
import services.ApiService;
import services.ValidadorContrato;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PassosApi {

    @Dado("um administrador autenticado")
    public void administrador() {
        ApiService.entrar("admin@ecocompliance.com", "admin123");
        assertEquals("Login de administrador falhou: " + Contexto.corpo, 200, Contexto.status);
    }

    @Dado("um usuário comum autenticado")
    public void usuario() {
        ApiService.entrar("user@ecocompliance.com", "user123");
        assertEquals("Login de usuário falhou: " + Contexto.corpo, 200, Contexto.status);
    }

    @Dado("uma empresa cadastrada")
    public void empresaCadastrada() {
        ApiService.cadastrarEmpresa();
        assertEquals("Cadastro de empresa falhou: " + Contexto.corpo, 201, Contexto.status);
    }

    @Quando("ele cadastra uma empresa válida")
    public void cadastraEmpresa() {
        ApiService.cadastrarEmpresa();
    }

    @Quando("ele envia um cadastro de empresa sem nome")
    public void empresaSemNome() {
        ApiService.empresaSemNome();
    }

    @Quando("ele busca a empresa de id {int}")
    public void buscaEmpresa(int id) {
        ApiService.buscarEmpresa(id);
    }

    @Quando("o administrador entra com e-mail e senha")
    public void login() {
        ApiService.entrar("admin@ecocompliance.com", "admin123");
    }

    @Quando("eu entro sem senha")
    public void loginSemSenha() {
        ApiService.loginSemSenha();
    }

    @Quando("eu cadastro um usuário")
    public void cadastroUsuario() {
        ApiService.cadastrarUsuario();
    }

    @Quando("eu cadastro uma licença que vence em 15 dias")
    public void licenca() {
        ApiService.cadastrarLicenca();
    }

    @Quando("eu cadastro uma auditoria")
    public void auditoria() {
        ApiService.cadastrarAuditoria();
    }

    @Quando("eu cadastro uma emissão")
    public void emissao() {
        ApiService.cadastrarEmissao();
    }

    @Quando("eu cadastro uma compensação")
    public void compensacao() {
        ApiService.cadastrarCompensacao();
    }

    @Quando("eu cadastro um agendamento")
    public void agendamento() {
        ApiService.cadastrarAgendamento();
    }

    @Quando("eu consulto o recurso cadastrado")
    public void consulta() {
        ApiService.consultarRecurso();
    }

    @Quando("eu atualizo o recurso cadastrado")
    public void atualiza() {
        ApiService.atualizarRecurso();
    }

    @Quando("eu excluo o recurso cadastrado")
    public void exclui() {
        ApiService.excluirRecurso();
    }

    @Quando("eu listo {string}")
    public void listo(String caminho) {
        ApiService.listar(caminho);
    }

    @Quando("eu gero o relatório da empresa")
    public void relatorio() {
        ApiService.relatorio();
    }

    @Quando("eu consulto os indicadores")
    public void indicadores() {
        ApiService.indicadores();
    }

    @Então("a API responde {int}")
    public void status(int esperado) {
        assertEquals(Contexto.corpo, esperado, Contexto.status);
    }

    @E("o campo {string} vale {string}")
    public void campoVale(String campo, String valor) {
        assertEquals(valor, JsonPath.from(Contexto.corpo).getString(campo));
    }

    @E("o campo {string} está preenchido")
    public void campoPreenchido(String campo) {
        String valor = JsonPath.from(Contexto.corpo).getString(campo);
        assertNotNull(valor);
        assertFalse(valor.isBlank());
    }

    @E("o contrato {string} é respeitado")
    public void contrato(String arquivo) {
        ValidadorContrato.validarObjeto(arquivo);
    }

    @E("a página respeita o contrato {string}")
    public void pagina(String arquivo) {
        ValidadorContrato.validarPagina(arquivo);
    }

    @E("a lista respeita o contrato {string}")
    public void lista(String arquivo) {
        ValidadorContrato.validarLista(arquivo);
    }

    @E("o corpo vem vazio")
    public void corpoVazio() {
        assertTrue(Contexto.corpo == null || Contexto.corpo.isBlank());
    }

    @E("o cabeçalho Location aponta para o recurso")
    public void location() {
        assertNotNull(Contexto.location);
        assertTrue(Contexto.location.contains("/empresas/"));
    }
}
