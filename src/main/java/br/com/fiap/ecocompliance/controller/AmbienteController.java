package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.AmbienteResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ambiente")
public class AmbienteController {

    private final String ambiente;

    public AmbienteController(@Value("${ecocompliance.ambiente}") String ambiente) {
        this.ambiente = ambiente;
    }

    @GetMapping
    public AmbienteResponseDTO atual() {
        return new AmbienteResponseDTO("ecocompliance", ambiente);
    }
}
