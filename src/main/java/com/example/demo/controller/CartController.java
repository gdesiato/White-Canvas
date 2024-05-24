package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.CartService;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.ConsultancyService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private CartService cartService;
    private UserService userService;
    private ConsultancyService consultancyService;
    private CartItemRepository cartItemRepository;

    public CartController(CartService cartService, UserService userService, ConsultancyService consultancyService, CartItemRepository cartItemRepository){
        this.cartService = cartService;
        this.userService = userService;
        this.consultancyService = consultancyService;
        this.cartItemRepository = cartItemRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("Cart service initialized: " + cartService);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> viewCart(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getCart());
    }

    @PostMapping("/{userId}")
    @Transactional
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId,
                                          @RequestParam("serviceName") String serviceName,
                                          @RequestParam("quantity") Integer quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartService.addToCart(userId, serviceName, quantity);
        if (cart == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cart);
    }



}
