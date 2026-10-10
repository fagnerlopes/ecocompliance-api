package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.StatusLicenca;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Licença ambiental persistida.")
public record LicencaResponseDTO(
        @Schema(example = "10")
        Long id,
        @Schema(example = "1")
        Long empresaId,
        @Schema(example = "Verde Norte Industria Ltda")
        String empresaNome,
        @Schema(example = "Licença de Operação")
        String tipoLicenca,
        @Schema(example = "LO-2026-0042")
        String numeroLicenca,
        @Schema(example = "2026-01-15")
        LocalDate dataEmissao,
        @Schema(example = "2027-01-15")
        LocalDate dataValidade,
        @Schema(example = "ATIVA")
        StatusLicenca status,
        @Schema(example = "CETESB")
        String orgaoEmissor
) {}
