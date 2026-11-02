package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import model.Contexto;

import java.io.InputStream;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ValidadorContrato {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonSchemaFactory FABRICA = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

    private ValidadorContrato() {
    }

    public static void validarObjeto(String arquivo) {
        validarNo(ler(Contexto.corpo), arquivo);
    }

    public static void validarPagina(String schemaDoItem) {
        JsonNode no = ler(Contexto.corpo);
        validarNo(no, "pagina.json");
        assertFalse("A página precisa trazer ao menos um item", no.get("content").isEmpty());
        validarNo(no.get("content").get(0), schemaDoItem);
    }

    public static void validarLista(String schemaDoItem) {
        JsonNode no = ler(Contexto.corpo);
        assertTrue("A resposta precisa ser uma lista. Corpo: " + Contexto.corpo, no.isArray());
        assertFalse("A lista precisa trazer ao menos um item. Corpo: " + Contexto.corpo, no.isEmpty());
        no.forEach(item -> validarNo(item, schemaDoItem));
    }

    private static JsonNode ler(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception ex) {
            throw new AssertionError("JSON inválido: " + json, ex);
        }
    }

    private static void validarNo(JsonNode no, String arquivo) {
        try (InputStream entrada = ValidadorContrato.class.getResourceAsStream("/schemas/" + arquivo)) {
            assertNotNull("Schema não encontrado: " + arquivo, entrada);
            JsonSchema schema = FABRICA.getSchema(MAPPER.readTree(entrada));
            Set<ValidationMessage> erros = schema.validate(no);
            assertTrue("Contrato " + arquivo + " violado: " + erros + " corpo: " + no, erros.isEmpty());
        } catch (AssertionError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AssertionError("Falha ao ler o schema " + arquivo, ex);
        }
    }
}
