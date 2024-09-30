package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.CartItemRequestDTO;
import com.desiato.whitecanvas.dto.CartRequestDTO;
import com.desiato.whitecanvas.dto.CartResponseDTO;
import com.desiato.whitecanvas.mapper.CartMapper;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.UserService;
import com.desiato.whitecanvas.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final CartMapper toDto;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        CartResponseDTO cartResponseDTO = toDto.toCartResponseDTO(cart, userId);
        return ResponseEntity.ok(cartResponseDTO);
    }

    @PostMapping("/{userId}/addToCart")
    public ResponseEntity<CartResponseDTO> addToCart(
            @PathVariable Long userId,
            @RequestBody CartRequestDTO cartRequestDTO) {
        User user = userService.getUserById(userId);
        Cart cart = user.getCart();

        for (CartItemRequestDTO item : cartRequestDTO.items()) {
            if (item.consultancyProduct() == null || item.quantity() <= 0) {
                throw new IllegalArgumentException("in the controller, Invalid product or quantity");
            }
            cartService.addToCart(cart, item);
        }
        Cart updatedCart = cartService.getCartById(cart.getId());
        CartResponseDTO response = toDto.toCartResponseDTO(updatedCart, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/emptyCart")
    public ResponseEntity<Void> emptyCart(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Cart cart = user.getCart();
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            cartService.clearCart(cart);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
