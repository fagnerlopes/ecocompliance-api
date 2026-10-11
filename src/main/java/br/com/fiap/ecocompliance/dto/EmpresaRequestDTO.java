package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar ou atualizar uma empresa.")
public record EmpresaRequestDTO(

        @Schema(description = "Razão social ou nome fantasia.", example = "Verde Norte Industria Ltda", minLength = 2, maxLength = 150)
        @NotBlank(message = "O nome da empresa é obrigatório")
        @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
        String nome,

        @Schema(description = "CNPJ único, com máscara ou só dígitos. Não há validação dos dígitos verificadores.", example = "12.345.678/0001-90")
        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(
                regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14}",
                message = "CNPJ inválido. Use o formato 00.000.000/0000-00 ou apenas dígitos"
        )
        String cnpj,

        @Schema(description = "Setor de atuação.", example = "Mineração", maxLength = 100)
        @NotBlank(message = "O setor é obrigatório")
        @Size(max = 100, message = "O setor deve ter no máximo 100 caracteres")
        String setor,

        @Schema(description = "E-mail do responsável ambiental.", example = "contato@verdenorte.com.br", maxLength = 150)
        @NotBlank(message = "O e-mail do responsável é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150)
        String emailResponsavel
) {}
