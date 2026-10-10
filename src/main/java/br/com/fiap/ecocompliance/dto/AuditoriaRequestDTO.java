package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.NivelRisco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Auditoria ambiental de uma empresa. O risco aceita BAIXO, MEDIO ou ALTO.")
public record AuditoriaRequestDTO(

        @Schema(description = "Empresa auditada.", example = "1")
        @NotNull(message = "O ID da empresa é obrigatório")
        @Positive
        Long empresaId,

        @Schema(description = "Data da visita. Não pode ser futura.", example = "2026-03-20")
        @NotNull(message = "A data da auditoria é obrigatória")
        @PastOrPresent(message = "A data da auditoria deve ser passada ou presente")
        LocalDate dataAuditoria,

        @Schema(description = "Conclusão resumida da auditoria.", example = "Não conformidades no armazenamento de resíduos", maxLength = 200)
        @NotBlank(message = "O resultado é obrigatório")
        @Size(max = 200)
        String resultado,

        @Schema(description = "Detalhes opcionais.", example = "A área de triagem estava sem contenção.", maxLength = 1000)
        @Size(max = 1000)
        String observacoes,

        @Schema(description = "Nível de risco: BAIXO, MEDIO ou ALTO.", example = "ALTO")
        @NotNull(message = "O nível de risco é obrigatório")
        NivelRisco nivelRisco
) {}
