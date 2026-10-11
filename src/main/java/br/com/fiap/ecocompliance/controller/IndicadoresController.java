package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.IndicadoresSustentabilidadeDTO;
import br.com.fiap.ecocompliance.service.IndicadoresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Indicadores", description = "Indicadores globais. Inclui agendamentos, ao contrário do relatório por empresa.")
@RestController
@RequestMapping("/indicadores-sustentabilidade")
@RequiredArgsConstructor
public class IndicadoresController {

    private final IndicadoresService indicadoresService;

    @Operation(summary = "Calcular indicadores globais", description = "Totais de empresas, emissões, compensações, saldo líquido, licenças, auditorias de alto risco e agendamentos pendentes ou em andamento.")
    @GetMapping
    public ResponseEntity<IndicadoresSustentabilidadeDTO> indicadores() {
        return ResponseEntity.ok(indicadoresService.calcular());
    }
}
