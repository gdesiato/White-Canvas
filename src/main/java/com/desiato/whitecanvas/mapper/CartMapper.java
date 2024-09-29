package com.desiato.whitecanvas.mapper;

import com.desiato.whitecanvas.dto.CartItemResponseDTO;
import com.desiato.whitecanvas.dto.CartResponseDTO;
import com.desiato.whitecanvas.model.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartResponseDTO toCartResponseDTO(Cart cart, Long userId) {
        List<CartItemResponseDTO> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponseDTO(
                        item.getProduct(),   // ConsultancyProduct from the item
                        item.getTotalPrice(),  // Total price calculated from the quantity and product price
                        item.getQuantity())   // Quantity of the product
                )
                .toList();

        return new CartResponseDTO(
                cart.getId(),
                userId,
                items,
                cart.getTotalPrice()  // This needs to be calculated in your Cart class, similar to CartItem's getTotalPrice
        );
    }
}
