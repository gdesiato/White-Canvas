package com.desiato.whitecanvas.mapper;

import com.desiato.whitecanvas.dto.CartResponseDTO;
import com.desiato.whitecanvas.model.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartResponseDTO toCartResponseDTO(Cart cart, Long userId) {
        return new CartResponseDTO(
                cart.getId(),
                userId,
                cart.getTotalPrice()
        );
    }
}
