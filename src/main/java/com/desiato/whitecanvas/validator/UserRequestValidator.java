package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.UserRequestDto;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRequestValidator {

    public void validateUserRequestDto(UserRequestDto userRequestDto) {
        List<String> errorMessages = new ArrayList<>();

        // Validate email if it's present
        if (userRequestDto.email() != null) {
            if (userRequestDto.email().isBlank()) {
                errorMessages.add("Email cannot be blank");
            } else if (!userRequestDto.email().contains("@")) {
                errorMessages.add("Invalid email format");
            }
        }

        // Validate password if it's present
        if (userRequestDto.password() != null) {
            if (userRequestDto.password().isBlank()) {
                errorMessages.add("Password cannot be blank");
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
