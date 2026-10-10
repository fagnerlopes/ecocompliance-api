package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.AmbienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ambiente", description = "Consulta pública do ambiente em que a API está publicada.")
@SecurityRequirements
@RestController
@RequestMapping("/ambiente")
public class AmbienteController {

    private final String ambiente;

    public AmbienteController(@Value("${ecocompliance.ambiente}") String ambiente) {
        this.ambiente = ambiente;
    }

    @Operation(summary = "Consultar ambiente", description = "Público. Devolve o nome da aplicação e o valor de APP_ENV: local, staging ou production.")
    @GetMapping
    public AmbienteResponseDTO atual() {
        return new AmbienteResponseDTO("ecocompliance", ambiente);
    }
}
