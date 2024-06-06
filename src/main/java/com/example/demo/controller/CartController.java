package com.example.demo.controller;

import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.CartService;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.ConsultancyService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    public CartController(CartService cartService, UserService userService){
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> viewCart(@PathVariable Long id) {
        User user = userService.getUserByUserId(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getCart());
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
