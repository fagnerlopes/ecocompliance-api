package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Emissão de carbono persistida.")
public record EmissaoResponseDTO(
        @Schema(example = "7")
        Long id,
        @Schema(example = "1")
        Long empresaId,
        @Schema(example = "Verde Norte Industria Ltda")
        String empresaNome,
        @Schema(example = "1280.50")
        BigDecimal quantidadeCo2,
        @Schema(example = "Caldeira a gás natural")
        String fonteEmissao,
        @Schema(example = "2026-02-28")
        LocalDate dataRegistro,
        @Schema(example = "tCO2e")
        String unidadeMedida
) {}
