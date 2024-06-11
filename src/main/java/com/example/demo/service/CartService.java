package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Consultancy;
import com.example.demo.model.User;
import com.example.demo.repository.CartItermRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ConsultancyRepository;
import com.example.demo.repository.UserRepository;
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
    private final ConsultancyRepository consultancyRepository;
    private final CartItermRepository cartItermRepository;

    public Cart getShoppingCartForUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (user == null) {
            return null;
        }
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart createCart(User user) {
        if (user == null) {
            return null;
        }
        Cart cart = new Cart(user);
        user.setCart(cart);
        cartRepository.save(cart);
        return cart;
    }

    public Cart addToCart(Cart cart, String serviceName, Integer quantity) throws Exception {
        if (cart == null || serviceName == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid cart, service name, or quantity");
        }

        Consultancy service = consultancyRepository.findByConsultancyName(serviceName);
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

    public Cart findCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            return null;
        }
        return cart;
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
}
