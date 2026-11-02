package services;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Contexto;
import model.EmpresaModel;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static io.restassured.RestAssured.given;

public class ApiService {

    public static final String BASE = System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080");
    private static final Gson GSON = new Gson();
    private static final AtomicLong SEQUENCIA = new AtomicLong(System.currentTimeMillis());

    private ApiService() {
    }

    public static void entrar(String email, String senha) {
        String json = """
                {"email":"%s","senha":"%s"}
                """.formatted(email, senha);
        chamar("POST", "/auth/login", json, false);
        if (Contexto.status == 200) {
            Contexto.token = io.restassured.path.json.JsonPath.from(Contexto.corpo).getString("token");
        }
    }

    public static void cadastrarEmpresa() {
        EmpresaModel empresa = new EmpresaModel();
        empresa.setNome("Verde Norte " + SEQUENCIA.incrementAndGet());
        empresa.setCnpj(cnpj());
        empresa.setSetor("Mineração");
        empresa.setEmailResponsavel("contato@verdenorte.com.br");
        String json = GSON.toJson(empresa);
        chamar("POST", "/empresas", json, true);
        if (Contexto.status == 201) {
            long id = io.restassured.path.json.JsonPath.from(Contexto.corpo).getLong("id");
            Contexto.empresaId = id;
            Contexto.recursoCaminho = "/empresas/" + id;
            Contexto.ultimoJson = json;
        }
    }

    public static void empresaSemNome() {
        String json = """
                {"nome":"","cnpj":"%s","setor":"Mineração","emailResponsavel":"contato@verdenorte.com.br"}
                """.formatted(cnpj());
        chamar("POST", "/empresas", json, true);
    }

    public static void cadastrarUsuario() {
        long seq = SEQUENCIA.incrementAndGet();
        String json = """
                {"nome":"Analista QA","email":"qa.%s@ecocompliance.test","senha":"senha123","role":"USER"}
                """.formatted(seq);
        chamar("POST", "/auth/register", json, false);
    }

    public static void loginSemSenha() {
        chamar("POST", "/auth/login", "{\"email\":\"admin@ecocompliance.com\",\"senha\":\"\"}", false);
    }

    public static void cadastrarLicenca() {
        String json = """
                {"empresaId":%d,"tipoLicenca":"Licença de Operação","numeroLicenca":"LO-%d","dataEmissao":"%s","dataValidade":"%s","status":"ATIVA","orgaoEmissor":"CETESB"}
                """.formatted(Contexto.empresaId, SEQUENCIA.incrementAndGet(), LocalDate.now(), LocalDate.now().plusDays(15));
        criarFilho("/licencas", json);
    }

    public static void cadastrarAuditoria() {
        String json = """
                {"empresaId":%d,"dataAuditoria":"%s","resultado":"Não conformidades no armazenamento de resíduos","observacoes":"A área de triagem estava sem contenção.","nivelRisco":"ALTO"}
                """.formatted(Contexto.empresaId, LocalDate.now().minusDays(1));
        criarFilho("/auditorias", json);
    }

    public static void cadastrarEmissao() {
        String json = """
                {"empresaId":%d,"quantidadeCo2":1280.50,"fonteEmissao":"Caldeira a gás natural","dataRegistro":"%s","unidadeMedida":"tCO2e"}
                """.formatted(Contexto.empresaId, LocalDate.now().minusDays(1));
        criarFilho("/emissoes", json);
    }

    public static void cadastrarCompensacao() {
        String json = """
                {"empresaId":%d,"tipoCompensacao":"Reflorestamento","descricao":"Plantio de 500 mudas nativas na APP do rio.","quantidadeCompensada":42.00,"dataAcao":"%s"}
                """.formatted(Contexto.empresaId, LocalDate.now().minusDays(1));
        criarFilho("/compensacoes", json);
    }

    public static void cadastrarAgendamento() {
        String json = """
                {"empresaId":%d,"titulo":"Troca da frota a diesel","descricao":"Substituir 12 caminhões por veículos elétricos.","tipoIniciativa":"Transporte","metaCo2":300.00,"dataInicio":"%s","dataFim":"%s","status":"PENDENTE"}
                """.formatted(Contexto.empresaId, LocalDate.now().plusDays(1), LocalDate.now().plusDays(40));
        criarFilho("/agendamentos-reducao", json);
    }

    public static void consultarRecurso() {
        chamar("GET", Contexto.recursoCaminho, null, true);
    }

    public static void atualizarRecurso() {
        chamar("PUT", Contexto.recursoCaminho, Contexto.ultimoJson, true);
    }

    public static void excluirRecurso() {
        chamar("DELETE", Contexto.recursoCaminho, null, true);
    }

    public static void listar(String caminho) {
        String resolvido = caminho.replace("{empresaId}", String.valueOf(Contexto.empresaId));
        chamar("GET", resolvido, null, true);
    }

    public static void relatorio() {
        chamar("GET", "/relatorios/empresa/" + Contexto.empresaId, null, true);
    }

    public static void indicadores() {
        chamar("GET", "/indicadores-sustentabilidade", null, true);
    }

    public static void buscarEmpresa(long id) {
        chamar("GET", "/empresas/" + id, null, true);
    }

    private static void criarFilho(String colecao, String json) {
        chamar("POST", colecao, json, true);
        if (Contexto.status == 201) {
            long id = io.restassured.path.json.JsonPath.from(Contexto.corpo).getLong("id");
            Contexto.recursoCaminho = colecao + "/" + id;
            Contexto.ultimoJson = json;
        }
    }

    private static String cnpj() {
        return String.format("%014d", SEQUENCIA.incrementAndGet() % 100000000000000L);
    }

    private static void chamar(String metodo, String caminho, String json, boolean autenticar) {
        try {
            RequestSpecification spec = given()
                    .baseUri(BASE)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON);
            if (autenticar && Contexto.token != null) {
                spec.header("Authorization", "Bearer " + Contexto.token);
            }
            if (json != null) {
                spec.body(json);
            }
            Response resposta = switch (metodo) {
                case "POST" -> spec.when().post(caminho);
                case "PUT" -> spec.when().put(caminho);
                case "DELETE" -> spec.when().delete(caminho);
                default -> spec.when().get(caminho);
            };
            Contexto.status = resposta.statusCode();
            Contexto.corpo = resposta.getBody() == null ? "" : resposta.getBody().asString();
            Contexto.location = resposta.getHeader("Location");
        } catch (Exception ex) {
            throw new AssertionError(
                    "Não foi possível chamar " + BASE + caminho + ". Suba a API com docker compose up --build. " + ex.getMessage(),
                    ex);
        }
    }
}
