package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Registro de emissão de carbono. Não há campo de escopo GHG.")
public record EmissaoRequestDTO(

        @Schema(description = "Empresa emissora.", example = "1")
        @NotNull(message = "O ID da empresa é obrigatório")
        @Positive
        Long empresaId,

        @Schema(description = "Quantidade emitida, maior que zero.", example = "1280.50")
        @NotNull(message = "A quantidade de CO2 é obrigatória")
        @Positive(message = "A quantidade de CO2 deve ser positiva")
        BigDecimal quantidadeCo2,

        @Schema(description = "Origem da emissão.", example = "Caldeira a gás natural", maxLength = 150)
        @NotBlank(message = "A fonte de emissão é obrigatória")
        @Size(max = 150)
        String fonteEmissao,

        @Schema(description = "Data do registro. Não pode ser futura.", example = "2026-02-28")
        @NotNull(message = "A data de registro é obrigatória")
        @PastOrPresent(message = "A data de registro deve ser passada ou presente")
        LocalDate dataRegistro,

        @Schema(description = "Unidade da quantidade.", example = "tCO2e", maxLength = 20)
        @NotBlank(message = "A unidade de medida é obrigatória")
        @Size(max = 20)
        String unidadeMedida
) {}
