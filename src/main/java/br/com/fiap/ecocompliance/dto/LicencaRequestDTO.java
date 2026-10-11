package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.StatusLicenca;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Licença ambiental vinculada a uma empresa. A validade não pode ser anterior à emissão.")
public record LicencaRequestDTO(

        @Schema(description = "Empresa titular da licença.", example = "1")
        @NotNull(message = "O ID da empresa é obrigatório")
        @Positive(message = "O ID da empresa deve ser positivo")
        Long empresaId,

        @Schema(description = "Tipo da licença.", example = "Licença de Operação", maxLength = 100)
        @NotBlank(message = "O tipo de licença é obrigatório")
        @Size(max = 100)
        String tipoLicenca,

        @Schema(description = "Número oficial da licença.", example = "LO-2026-0042", maxLength = 50)
        @NotBlank(message = "O número da licença é obrigatório")
        @Size(max = 50)
        String numeroLicenca,

        @Schema(description = "Data de emissão. Não pode ser futura.", example = "2026-01-15")
        @NotNull(message = "A data de emissão é obrigatória")
        @PastOrPresent(message = "A data de emissão deve ser passada ou presente")
        LocalDate dataEmissao,

        @Schema(description = "Data de validade. Deve ser igual ou posterior à emissão.", example = "2027-01-15")
        @NotNull(message = "A data de validade é obrigatória")
        LocalDate dataValidade,

        @Schema(description = "Situação informada pelo cliente. A API não recalcula o status sozinha.", example = "ATIVA")
        @NotNull(message = "O status é obrigatório")
        StatusLicenca status,

        @Schema(description = "Órgão que emitiu a licença.", example = "CETESB", maxLength = 100)
        @NotBlank(message = "O órgão emissor é obrigatório")
        @Size(max = 100)
        String orgaoEmissor
) {}
