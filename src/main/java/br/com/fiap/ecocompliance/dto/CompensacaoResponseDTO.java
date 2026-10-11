package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Compensação ambiental persistida.")
public record CompensacaoResponseDTO(
        @Schema(example = "3")
        Long id,
        @Schema(example = "1")
        Long empresaId,
        @Schema(example = "Verde Norte Industria Ltda")
        String empresaNome,
        @Schema(example = "Reflorestamento")
        String tipoCompensacao,
        @Schema(example = "Plantio de 500 mudas nativas na APP do rio.")
        String descricao,
        @Schema(example = "42.00")
        BigDecimal quantidadeCompensada,
        @Schema(example = "2026-04-02")
        LocalDate dataAcao
) {}
