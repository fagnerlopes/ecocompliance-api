package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.AuthResponseDTO;
import br.com.fiap.ecocompliance.dto.LoginRequestDTO;
import br.com.fiap.ecocompliance.dto.RegisterRequestDTO;
import br.com.fiap.ecocompliance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Cadastro e login. Os dois endpoints são públicos e devolvem JWT.")
@SecurityRequirements
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Cadastrar usuário", description = "Público. Aceita a role no corpo e já devolve o JWT. E-mail repetido responde 409. A senha é gravada só como hash BCrypt.")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@RequestBody @Valid RegisterRequestDTO dto) {
        return authService.register(dto);
    }

    @Operation(summary = "Entrar", description = "Público. Confere e-mail e senha e devolve o JWT. Credencial inválida responde 401.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
