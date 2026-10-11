package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.StatusAgendamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Agendamento de redução de carbono persistido.")
public record AgendamentoResponseDTO(
        @Schema(example = "2")
        Long id,
        @Schema(example = "1")
        Long empresaId,
        @Schema(example = "Verde Norte Industria Ltda")
        String empresaNome,
        @Schema(example = "Troca da frota a diesel")
        String titulo,
        @Schema(example = "Substituir 12 caminhões por veículos elétricos.")
        String descricao,
        @Schema(example = "Transporte")
        String tipoIniciativa,
        @Schema(example = "300.00")
        BigDecimal metaCo2,
        @Schema(example = "2026-11-01")
        LocalDate dataInicio,
        @Schema(example = "2027-06-30")
        LocalDate dataFim,
        @Schema(example = "PENDENTE")
        StatusAgendamento status,
        @Schema(description = "Quando o agendamento foi criado.", example = "2026-10-10T18:40:00")
        LocalDateTime dataCriacao
) {}
