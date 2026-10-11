package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Ação de compensação ambiental ligada a uma empresa.")
public record CompensacaoRequestDTO(

        @Schema(description = "Empresa responsável pela compensação.", example = "1")
        @NotNull(message = "O ID da empresa é obrigatório")
        @Positive
        Long empresaId,

        @Schema(description = "Tipo da compensação.", example = "Reflorestamento", maxLength = 100)
        @NotBlank(message = "O tipo de compensação é obrigatório")
        @Size(max = 100)
        String tipoCompensacao,

        @Schema(description = "O que foi feito.", example = "Plantio de 500 mudas nativas na APP do rio.", maxLength = 500)
        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 500)
        String descricao,

        @Schema(description = "Quantidade compensada, maior que zero.", example = "42.00")
        @NotNull(message = "A quantidade compensada é obrigatória")
        @Positive(message = "A quantidade compensada deve ser positiva")
        BigDecimal quantidadeCompensada,

        @Schema(description = "Data da ação. Não pode ser futura.", example = "2026-04-02")
        @NotNull(message = "A data da ação é obrigatória")
        @PastOrPresent(message = "A data da ação deve ser passada ou presente")
        LocalDate dataAcao
) {}
