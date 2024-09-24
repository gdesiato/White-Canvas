package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.UserService;
import com.desiato.whitecanvas.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/addToCart")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId,
                                          @RequestParam("consultancyName") String consultancyName,
                                          @RequestParam("quantity") Integer quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOptional = userService.getUser(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        Cart cart = user.getCart();
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        try {
            Cart updatedCart = cartService.addToCart(cart, consultancyName, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{userId}/emptyCart")
    public ResponseEntity<Void> emptyCart(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUser(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
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
