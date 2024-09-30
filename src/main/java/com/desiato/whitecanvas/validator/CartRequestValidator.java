package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.dto.CartItemRequestDTO;
import com.desiato.whitecanvas.dto.CartRequestDTO;
import com.desiato.whitecanvas.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartRequestValidator {

    public void validateCartRequestDto(CartRequestDTO cartRequestDTO) {
        List<String> errorMessages = new ArrayList<>();

        // Validate that the cart has items
        if (cartRequestDTO.items() == null || cartRequestDTO.items().isEmpty()) {
            errorMessages.add("Cart must contain at least one item.");
        } else {
            // Validate each CartItemRequestDTO
            for (CartItemRequestDTO item : cartRequestDTO.items()) {
                if (item.consultancyProduct() == null) {
                    errorMessages.add("Consultancy product must not be null.");
                }
                if (item.quantity() <= 0) {
                    errorMessages.add("Quantity must be greater than 0.");
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
