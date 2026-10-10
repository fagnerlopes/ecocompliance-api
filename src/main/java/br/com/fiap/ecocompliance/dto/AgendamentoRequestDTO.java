package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.StatusAgendamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Plano de redução de carbono. A data fim precisa ser posterior à data início. Status CONCLUIDO ou CANCELADO não pode ser editado.")
public record AgendamentoRequestDTO(

        @Schema(description = "Empresa do plano.", example = "1")
        @NotNull(message = "O ID da empresa é obrigatório")
        @Positive
        Long empresaId,

        @Schema(description = "Nome curto da iniciativa.", example = "Troca da frota a diesel", maxLength = 150)
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150)
        String titulo,

        @Schema(description = "O que será feito.", example = "Substituir 12 caminhões por veículos elétricos.", maxLength = 500)
        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 500)
        String descricao,

        @Schema(description = "Categoria da iniciativa.", example = "Transporte", maxLength = 50)
        @NotBlank(message = "O tipo de iniciativa é obrigatório")
        @Size(max = 50)
        String tipoIniciativa,

        @Schema(description = "Meta de redução, maior que zero.", example = "300.00")
        @NotNull(message = "A meta de redução de CO2 é obrigatória")
        @Positive(message = "A meta de CO2 deve ser positiva")
        BigDecimal metaCo2,

        @Schema(description = "Início do plano. Não pode ser no passado.", example = "2026-11-01")
        @NotNull(message = "A data de início é obrigatória")
        @FutureOrPresent(message = "A data de início não pode ser no passado")
        LocalDate dataInicio,

        @Schema(description = "Fim do plano. Deve ser futura e posterior ao início.", example = "2027-06-30")
        @NotNull(message = "A data de fim é obrigatória")
        @Future(message = "A data de fim deve ser futura")
        LocalDate dataFim,

        @Schema(description = "Situação. Se omitido, o serviço define o padrão. Valores: PENDENTE, EM_ANDAMENTO, CONCLUIDO, CANCELADO.", example = "PENDENTE")
        StatusAgendamento status
) {}
