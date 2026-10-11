package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Indicadores globais. Diferente do relatório por empresa, este cálculo inclui os agendamentos.")
public record IndicadoresSustentabilidadeDTO(
        @Schema(description = "Empresas cadastradas.", example = "4")
        long totalEmpresas,
        @Schema(description = "Soma de todas as emissões.", example = "5400.00")
        BigDecimal totalEmissoesCo2,
        @Schema(description = "Soma de todas as compensações.", example = "800.00")
        BigDecimal totalCarbonoCompensado,
        @Schema(description = "Emissões menos compensações.", example = "4600.00")
        BigDecimal saldoLiquidoCarbono,
        @Schema(example = "6")
        long licencasAtivas,
        @Schema(example = "2")
        long licencasVencidas,
        @Schema(example = "9")
        long totalAuditorias,
        @Schema(description = "Auditorias com risco ALTO.", example = "3")
        long auditoriasAltoRisco,
        @Schema(description = "Agendamentos com status EM_ANDAMENTO.", example = "1")
        long agendamentosEmAndamento,
        @Schema(description = "Agendamentos com status PENDENTE.", example = "2")
        long agendamentosPendentes
) {}
