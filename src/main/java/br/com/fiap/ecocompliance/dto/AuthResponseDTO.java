package br.com.fiap.ecocompliance.dto;

import br.com.fiap.ecocompliance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT e os dados públicos do usuário autenticado.")
public record AuthResponseDTO(
        @Schema(description = "JWT para o cabeçalho Authorization: Bearer.", example = "eyJhbGciOiJIUzI1NiJ9.exemplo")
        String token,
        @Schema(description = "Tipo do token.", example = "Bearer")
        String tipo,
        @Schema(example = "admin@ecocompliance.com")
        String email,
        @Schema(example = "Administrador")
        String nome,
        @Schema(example = "ADMIN")
        Role role
) {
    public static AuthResponseDTO of(String token, String email, String nome, Role role) {
        return new AuthResponseDTO(token, "Bearer", email, nome, role);
    }
}
