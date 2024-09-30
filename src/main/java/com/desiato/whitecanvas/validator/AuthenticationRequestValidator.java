package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationRequestValidator {

    public void validateAuthenticationRequestDto(AuthenticationRequestDto request) {
        List<String> errorMessages = new ArrayList<>();

        // Validate email
        if (request.email() == null || request.email().isBlank()) {
            errorMessages.add("Email cannot be blank.");
        } else if (!request.email().contains("@")) {
            errorMessages.add("Invalid email format.");
        }

        // Validate password
        if (request.password() == null || request.password().isBlank()) {
            errorMessages.add("Password cannot be blank.");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
