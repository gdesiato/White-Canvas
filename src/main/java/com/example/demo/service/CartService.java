package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Consultancy;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ConsultancyRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ConsultancyRepository consultancyRepository;
    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ConsultancyRepository consultancyRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.consultancyRepository = consultancyRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Cart cart) {
        for (CartItem item : cart.getCartItems()) {
            cartItemRepository.delete(item);
        }
        cart.getCartItems().clear();
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
