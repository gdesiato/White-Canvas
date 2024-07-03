package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.ConsultancyService;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.CartItemRepository;
import com.desiato.whitecanvas.repository.CartRepository;
import com.desiato.whitecanvas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public Cart addToCart(Cart cart, String serviceName, Integer quantity) {
        if (cart == null || serviceName == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid cart, service name, or quantity");
        }

        ConsultancyService service;
        try {
            service = ConsultancyService.valueOf(serviceName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }

        // Find existing cart item for the same service
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getService().equals(service))
                .findFirst()
                .orElse(null);

        if (existingCartItem == null) {
            // No existing cart item, create a new one
            CartItem newCartItem = new CartItem();
            newCartItem.setService(service);
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);
            cart.addCartItem(newCartItem);
            cartItemRepository.save(newCartItem);
        } else {
            // Existing cart item found, increase quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        }

        return cartRepository.save(cart);
    }

    public void removeItemFromCart(Cart cart, CartItem cartItem) {
        cart.getCartItems().remove(cartItem);
        cartItem.setCart(null);

        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    public void clearCart(Cart cart) {
        List<CartItem> itemsToRemove = new ArrayList<>(cart.getCartItems());
        for (CartItem item : itemsToRemove) {
            cartItemRepository.delete(item);
        }
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId) {
        // Find the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the user's cart
        return user.getCart();
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
}
