package br.com.fiap.ecocompliance.exception;

import br.com.fiap.ecocompliance.controller.AuthController;
import br.com.fiap.ecocompliance.dto.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/recurso");
    }

    @Test
    @DisplayName("recurso inexistente deve responder 404")
    void resourceNotFound_retorna404() {
        ResponseEntity<ErrorResponse> resposta = handler.handleResourceNotFound(
                new ResourceNotFoundException("Empresa não encontrada"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resposta.getBody().message()).isEqualTo("Empresa não encontrada");
        assertThat(resposta.getBody().path()).isEqualTo("/recurso");
    }

    @Test
    @DisplayName("regra de negócio deve responder 400")
    void business_retorna400() {
        ResponseEntity<ErrorResponse> resposta = handler.handleBusiness(
                new BusinessException("CNPJ duplicado"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resposta.getBody().message()).isEqualTo("CNPJ duplicado");
    }

    @Test
    @DisplayName("e-mail duplicado deve responder 409")
    void emailExists_retorna409() {
        ResponseEntity<ErrorResponse> resposta = handler.handleEmailExists(
                new EmailAlreadyExistsException("E-mail já cadastrado"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resposta.getBody().message()).contains("E-mail já cadastrado");
    }

    @Test
    @DisplayName("validação de campos deve responder 400 com a lista de erros")
    void validation_retorna400ComCampos() throws Exception {
        Method method = AuthController.class.getDeclaredMethod("register", RegisterRequestDTO.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "dto");
        binding.addError(new FieldError("dto", "email", "E-mail inválido"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, binding);

        ResponseEntity<ErrorResponse> resposta = handler.handleValidation(ex, request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resposta.getBody().message()).isEqualTo("Erro de validação nos campos");
        assertThat(resposta.getBody().fieldErrors()).hasSize(1);
        assertThat(resposta.getBody().fieldErrors().get(0).campo()).isEqualTo("email");
        assertThat(resposta.getBody().fieldErrors().get(0).mensagem()).isEqualTo("E-mail inválido");
    }

    @Test
    @DisplayName("credencial inválida deve responder 401")
    void badCredentials_retorna401() {
        ResponseEntity<ErrorResponse> resposta = handler.handleBadCredentials(
                new BadCredentialsException("bad"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(resposta.getBody().message()).isEqualTo("Credenciais inválidas");
    }

    @Test
    @DisplayName("falha genérica de autenticação deve responder 401")
    void authentication_retorna401() {
        ResponseEntity<ErrorResponse> resposta = handler.handleAuthentication(
                new InsufficientAuthenticationException("token ausente"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(resposta.getBody().message()).contains("token ausente");
    }

    @Test
    @DisplayName("acesso negado deve responder 403")
    void accessDenied_retorna403() {
        ResponseEntity<ErrorResponse> resposta = handler.handleAccessDenied(
                new AccessDeniedException("negado"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(resposta.getBody().message()).contains("permissão");
    }

    @Test
    @DisplayName("violação de integridade deve responder 409")
    void dataIntegrity_retorna409() {
        ResponseEntity<ErrorResponse> resposta = handler.handleDataIntegrity(
                new DataIntegrityViolationException("fk"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resposta.getBody().message()).contains("integridade");
    }

    @Test
    @DisplayName("erro inesperado deve responder 500")
    void generic_retorna500() {
        ResponseEntity<ErrorResponse> resposta = handler.handleGeneric(
                new IllegalStateException("boom"), request);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resposta.getBody().message()).isEqualTo("Erro interno: boom");
    }
}
