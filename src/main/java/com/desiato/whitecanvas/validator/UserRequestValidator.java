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

        if (userRequestDto.email() == null || userRequestDto.email().isBlank()) {
            errorMessages.add("Email is required");
        } else if (!userRequestDto.email().contains("@")) {
            errorMessages.add("Invalid email format");
        }

        if (userRequestDto.password() == null || userRequestDto.password().isBlank()) {
            errorMessages.add("Password is required");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
