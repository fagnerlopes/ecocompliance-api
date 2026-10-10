package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Cadastro de usuário. A role vem no corpo e o endpoint devolve o JWT.")
public record RegisterRequestDTO(

        @Schema(description = "Nome do usuário.", example = "Ana Lima", minLength = 2, maxLength = 150)
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 150)
        String nome,

        @Schema(description = "E-mail único.", example = "ana.lima@ecocompliance.com", maxLength = 150)
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150)
        String email,

        @Schema(description = "Senha em texto, de 6 a 100 caracteres. Só o hash é gravado.", example = "senha123", minLength = 6, maxLength = 100)
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @Schema(description = "Papel de acesso. ADMIN escreve; USER só consulta.", example = "USER")
        @NotNull(message = "A role é obrigatória (ADMIN ou USER)")
        Role role
) {}
