package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.OrderRequestDTO;
import com.desiato.whitecanvas.exception.ErrorMessage;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderRequestValidator extends AbstractValidator<OrderRequestDTO> {

    @Override
    protected void validate(OrderRequestDTO request, List<ErrorMessage> errorMessages) {

        if (request.userId() == null) {
            errorMessages.add(new ErrorMessage("User ID is required"));
        }

        if (request.items() == null || request.items().isEmpty()) {
            errorMessages.add(new ErrorMessage("Order items cannot be null or empty"));
        } else {
            for (int i = 0; i < request.items().size(); i++) {
                if (request.items().get(i) == null) {
                    errorMessages.add(new ErrorMessage("Order item at index " + i + " cannot be null"));
                }
            }
        }

        if (request.orderDate() == null) {
            errorMessages.add(new ErrorMessage("Order date is required"));
        } else if (request.orderDate().isAfter(LocalDateTime.now())) {
            errorMessages.add(new ErrorMessage("Order date cannot be in the future"));
        }

        if (request.totalAmount() == null) {
            errorMessages.add(new ErrorMessage("Total amount is required"));
        } else if (request.totalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errorMessages.add(new ErrorMessage("Total amount must be positive"));
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
