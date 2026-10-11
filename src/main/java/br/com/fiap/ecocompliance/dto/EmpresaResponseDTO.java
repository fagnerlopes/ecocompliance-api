package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Empresa cadastrada.")
public record EmpresaResponseDTO(
        @Schema(description = "Identificador gerado pelo banco.", example = "1")
        Long id,
        @Schema(example = "Verde Norte Industria Ltda")
        String nome,
        @Schema(example = "12.345.678/0001-90")
        String cnpj,
        @Schema(example = "Mineração")
        String setor,
        @Schema(example = "contato@verdenorte.com.br")
        String emailResponsavel,
        @Schema(description = "Data e hora do cadastro.", example = "2026-10-10T18:30:00")
        LocalDateTime createdAt
) {}
