package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.RelatorioEmpresaDTO;
import br.com.fiap.ecocompliance.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Relatórios", description = "Consolidado por empresa. Não inclui agendamentos de redução.")
@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Operation(summary = "Gerar relatório da empresa", description = "Soma emissões e compensações, conta licenças ativas e vencidas, conta auditorias e informa o maior risco. Empresa inexistente responde 404.")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<RelatorioEmpresaDTO> relatorioPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioEmpresa(empresaId));
    }
}
