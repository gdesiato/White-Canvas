package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.CartItemRequestDTO;
import com.desiato.whitecanvas.dto.CartRequestDTO;
import com.desiato.whitecanvas.exception.ErrorMessage;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartRequestValidator extends AbstractValidator<CartRequestDTO> {

    @Override
    protected void validate(CartRequestDTO request, List<ErrorMessage> errorMessages) {

        // Validate that the cart has items
        if (request.items() == null || request.items().isEmpty()) {
            errorMessages.add(new ErrorMessage("Cart must contain at least one item."));
        } else {
            // Validate each CartItemRequestDTO
            for (CartItemRequestDTO item : request.items()) {
                if (item.consultancyProduct() == null) {
                    errorMessages.add(new ErrorMessage("Consultancy product must not be null."));
                }
                if (item.quantity() <= 0) {
                    errorMessages.add(new ErrorMessage("Quantity must be greater than 0."));
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
