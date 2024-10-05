package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.exception.ErrorMessage;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationRequestValidator extends AbstractValidator<AuthenticationRequestDto>{

    @Override
    protected void validate(AuthenticationRequestDto request, List<ErrorMessage> errorMessages) {

        // Validate email
        if (request.email() == null || request.email().isBlank()) {
            errorMessages.add(new ErrorMessage("Email cannot be blank."));
        } else if (!request.email().contains("@")) {
            errorMessages.add(new ErrorMessage("Invalid email format."));
        }

        // Validate password
        if (request.password() == null || request.password().isBlank()) {
            errorMessages.add(new ErrorMessage("Password cannot be blank."));
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
