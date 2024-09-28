package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.OrderRequestDTO;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRequestValidator {

    public void validateOrderRequestDto(OrderRequestDTO orderRequestDTO) {
        List<String> errorMessages = new ArrayList<>();

        if (orderRequestDTO.userId() == null) {
            errorMessages.add("User ID is required");
        }

        if (orderRequestDTO.items() == null || orderRequestDTO.items().isEmpty()) {
            errorMessages.add("Order items cannot be null or empty");
        } else {
            for (int i = 0; i < orderRequestDTO.items().size(); i++) {
                if (orderRequestDTO.items().get(i) == null) {
                    errorMessages.add("Order item at index " + i + " cannot be null");
                }
            }
        }

        if (orderRequestDTO.orderDate() == null) {
            errorMessages.add("Order date is required");
        } else if (orderRequestDTO.orderDate().isAfter(LocalDateTime.now())) {
            errorMessages.add("Order date cannot be in the future");
        }

        if (orderRequestDTO.totalAmount() == null) {
            errorMessages.add("Total amount is required");
        } else if (orderRequestDTO.totalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errorMessages.add("Total amount must be positive");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
