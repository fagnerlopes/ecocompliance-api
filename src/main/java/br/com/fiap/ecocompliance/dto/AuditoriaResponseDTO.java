package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.NivelRisco;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Auditoria ambiental persistida.")
public record AuditoriaResponseDTO(
        @Schema(example = "4")
        Long id,
        @Schema(example = "1")
        Long empresaId,
        @Schema(example = "Verde Norte Industria Ltda")
        String empresaNome,
        @Schema(example = "2026-03-20")
        LocalDate dataAuditoria,
        @Schema(example = "Não conformidades no armazenamento de resíduos")
        String resultado,
        @Schema(example = "A área de triagem estava sem contenção.")
        String observacoes,
        @Schema(example = "ALTO")
        NivelRisco nivelRisco
) {}
