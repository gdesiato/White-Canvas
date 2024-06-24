package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.ConsultancyService;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.CartItermRepository;
import com.desiato.whitecanvas.repository.CartRepository;
import com.desiato.whitecanvas.repository.ConsultancyServiceRepository;
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
    private final UserRepository userRepository;
    private final ConsultancyServiceRepository consultancyServiceRepository;
    private final CartItermRepository cartItermRepository;

    public Optional<Cart> getShoppingCartForUser(Long userId) {
        return Optional.ofNullable(cartRepository.findByUserId(userId))
                .or(() -> createCart(userId));
    }

    public Optional<Cart> createCart(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.flatMap(user -> {
            Cart cart = new Cart();
            cart.setUser(user);  // Set the user to the newly created cart
            user.setCart(cart);  // Set the cart to the user
            cartRepository.save(cart);  // Save the new cart
            return Optional.of(cart);   // Return the newly created cart wrapped in an Optional
        });
    }

    public Cart addToCart(Cart cart, String serviceName, Integer quantity) throws Exception {
        if (cart == null || serviceName == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid cart, service name, or quantity");
        }

        ConsultancyService service = consultancyServiceRepository.findByDescription(serviceName);
        if (service == null) {
            throw new Exception("Service not found");
        }

        // Find existing cart item for the same service
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getService().equals(service))
                .findFirst()
                .orElse(null);

        if (existingCartItem == null) {
            // No existing cart item, create a new one
            CartItem newCartItem = new CartItem(service, quantity);
            cart.addCartItem(newCartItem);
            cartItermRepository.save(newCartItem);
        } else {
            // Existing cart item found, increase quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItermRepository.save(existingCartItem);
        }

        return cartRepository.save(cart);
    }

    public void removeItemFromCart(Cart cart, CartItem cartItem) {
        cart.getCartItems().remove(cartItem);
        cartItermRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    public void clearCart(Cart cart) {
        List<CartItem> itemsToRemove = new ArrayList<>(cart.getCartItems());
        for (CartItem item : itemsToRemove) {
            cartItermRepository.delete(item);
        }
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public Optional<Cart> findCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        return Optional.ofNullable(cart);  // Convert the cart to an Optional, handling null automatically
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
}
