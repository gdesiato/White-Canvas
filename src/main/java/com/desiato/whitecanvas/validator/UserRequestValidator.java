package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.UserRequestDto;
import com.desiato.whitecanvas.exception.ErrorMessage;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRequestValidator extends AbstractValidator<UserRequestDto> {

    @Override
    protected void validate(UserRequestDto request, List<ErrorMessage> errorMessages) {

        if (request.email() != null) {
            if (request.email().isBlank()) {
                errorMessages.add(new ErrorMessage("Email cannot be blank"));
            } else if (!request.email().contains("@")) {
                errorMessages.add(new ErrorMessage("Invalid email format"));
            }
        }

        if (request.password() != null && request.password().isBlank()) {
                errorMessages.add(new ErrorMessage("Password cannot be blank"));
            }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
