package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Identifica em qual ambiente a API está rodando.")
public record AmbienteResponseDTO(
        @Schema(description = "Nome fixo da aplicação.", example = "ecocompliance")
        String aplicacao,
        @Schema(description = "Valor de APP_ENV: local, staging ou production.", example = "staging")
        String ambiente
) {}
