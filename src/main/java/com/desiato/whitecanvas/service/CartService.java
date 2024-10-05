package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.CartItemRequestDTO;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.CartItemRepository;
import com.desiato.whitecanvas.repository.CartRepository;
import com.desiato.whitecanvas.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart addToCart(Cart cart, CartItemRequestDTO cartItemRequestDTO) {

        if (cart == null) {
            throw new IllegalArgumentException("Invalid cart");
        }

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(cartItemRequestDTO.consultancyProduct()))
                .findFirst()
                .orElse(null);

        if (existingCartItem == null) {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(cartItemRequestDTO.consultancyProduct());
            newCartItem.setQuantity(cartItemRequestDTO.quantity());
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        } else {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequestDTO.quantity());
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

    @Transactional
    public void clearCart(Cart cart) {
        List<CartItem> itemsToRemove = new ArrayList<>(cart.getCartItems());

        for (CartItem item : itemsToRemove) {
            // Break the bidirectional relationship
            item.setCart(null);
            cartItemRepository.delete(item);
        }
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        // Return the user's cart
        return user.getCart();
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with ID: " + cartId + " not found"));
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
}
