package com.desiato.whitecanvas.exception;

import com.desiato.whitecanvas.dto.LoginResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleCustomerAlreadyExistException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, List<ErrorMessage>>> handleValidationException(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", ex.getErrorMessages()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<LoginResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(null, "Authentication failed: " + ex.getMessage()));
    }
}
