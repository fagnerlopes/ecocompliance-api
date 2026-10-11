package br.com.fiap.ecocompliance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Saúde", description = "Healthcheck usado pelo proxy e pelo App Service.")
@SecurityRequirements
@RestController
public class UpController {

    @Operation(summary = "Verificar se a API está no ar", description = "Público. Responde {\"status\":\"up\"} sem consultar o banco.")
    @GetMapping("/up")
    public Map<String, String> verificar() {
        return Map.of("status", "up");
    }
}
