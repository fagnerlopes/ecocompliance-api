package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.NivelRisco;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Consolidado de uma empresa. Não inclui agendamentos de redução.")
public record RelatorioEmpresaDTO(
        @Schema(description = "Cadastro da empresa.")
        EmpresaResponseDTO empresa,
        @Schema(description = "Soma das emissões registradas.", example = "1280.50")
        BigDecimal totalEmissoesCarbono,
        @Schema(description = "Soma das compensações registradas.", example = "42.00")
        BigDecimal totalCarbonoCompensado,
        @Schema(description = "Licenças com status ATIVA.", example = "2")
        Long quantidadeLicencasAtivas,
        @Schema(description = "Licenças com status VENCIDA.", example = "1")
        Long quantidadeLicencasVencidas,
        @Schema(description = "Total de auditorias da empresa.", example = "3")
        Long quantidadeAuditorias,
        @Schema(description = "Maior risco encontrado nas auditorias.", example = "ALTO")
        NivelRisco maiorNivelRisco
) {}
