package br.com.fiap.ecocompliance.contrato;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidadorJsonSchema {

    private final JsonSchemaFactory fabrica = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    private final ObjectMapper mapper = new ObjectMapper();

    public void validarObjeto(String json, String arquivo) {
        validarNo(ler(json), arquivo);
    }

    public void validarPagina(String json, String schemaDoItem) {
        JsonNode no = ler(json);
        validarNo(no, "pagina.json");
        assertFalse(no.get("content").isEmpty(), "A página precisa trazer ao menos um item");
        validarNo(no.get("content").get(0), schemaDoItem);
    }

    public void validarLista(String json, String schemaDoItem) {
        JsonNode no = ler(json);
        assertTrue(no.isArray(), "A resposta precisa ser uma lista");
        assertFalse(no.isEmpty(), "A lista precisa trazer ao menos um item");
        no.forEach(item -> validarNo(item, schemaDoItem));
    }

    private JsonNode ler(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException ex) {
            throw new AssertionError("JSON inválido: " + ex.getMessage(), ex);
        }
    }

    private void validarNo(JsonNode no, String arquivo) {
        try (InputStream entrada = getClass().getResourceAsStream("/schemas/" + arquivo)) {
            assertNotNull(entrada, "Schema não encontrado: " + arquivo);
            JsonSchema schema = fabrica.getSchema(mapper.readTree(entrada));
            Set<ValidationMessage> erros = schema.validate(no);
            assertTrue(erros.isEmpty(), "Contrato " + arquivo + " violado: " + erros);
        } catch (IOException ex) {
            throw new AssertionError("Falha ao ler o schema " + arquivo, ex);
        }
    }
}
