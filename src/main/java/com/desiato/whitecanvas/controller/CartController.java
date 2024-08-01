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
    @Operation(summary = "Get cart by user ID", description = "Retrieves the cart associated with the specified user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart",
                    content = @Content(schema = @Schema(implementation = Cart.class),
                            examples = @ExampleObject(value = "{ \"userId\": 1, \"items\": [] }"))),
            @ApiResponse(responseCode = "404", description = "User or Cart not found")
    })
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/addToCart")
    @Operation(summary = "Add item to cart", description = "Adds an item to the cart for the specified user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added item to cart",
                    content = @Content(schema = @Schema(implementation = Cart.class),
                            examples = @ExampleObject(value = "{ \"userId\": 1, \"items\": [{ \"name\": \"Body Shape\", \"quantity\": 2 }] }"))),
            @ApiResponse(responseCode = "400", description = "Invalid input, item not added"),
            @ApiResponse(responseCode = "404", description = "User or Cart not found")
    })
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
    @Operation(summary = "Empty the cart", description = "Empties the cart for the specified user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully emptied the cart"),
            @ApiResponse(responseCode = "404", description = "User or Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
