package br.com.fiap.ecocompliance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para obter um JWT.")
public record LoginRequestDTO(

        @Schema(description = "E-mail cadastrado.", example = "admin@ecocompliance.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(description = "Senha em texto. A API compara com o hash BCrypt.", example = "admin123")
        @NotBlank(message = "A senha é obrigatória")
        String senha
) {}
