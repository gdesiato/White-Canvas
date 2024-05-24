package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Consultancy;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ConsultancyRepository;
import com.example.demo.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private UserService userService;
    private UserRepository userRepository;
    private ConsultancyRepository serviceRepository;
    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, UserService userService, UserRepository userRepository, ConsultancyRepository serviceRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Cart getShoppingCartForUser(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Transactional
    public Cart createCart(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return null;
        }
        Cart cart = new Cart(user);
        user.setCart(cart);
        userRepository.save(user); // Save the User entity right after its Cart has been set
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart addToCart(Long userId, String serviceName, Integer quantity) {

        final Logger log = LogManager.getLogger(CartService.class);

        User user = userService.getUserById(userId);
        if (user == null) {
            log.info("User is null");
            return null;
        }

        Consultancy service = serviceRepository.findByConsultancyName(serviceName);
        if (service == null) {
            log.info("Service is null");
            return null;
        }

        Cart cart = user.getCart();

        log.info("Accessed cart with ID: {}", cart.getId());

        log.info("Cart before operation: {}", cart);

        // Check if there's already a CartItem for the same service in the cart
        CartItem existingCartItem = null;
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getService().equals(service)) {
                existingCartItem = cartItem;
                break;
            }
        }

        if (existingCartItem == null) {
            // If there's no existing CartItem for the service, create a new one
            CartItem cartItem = new CartItem(service, quantity);
            if (cartItem.getQuantity() <= 0) {
                return cart;
            }
            log.info("Adding a new CartItem: {}", cartItem);
            cart.addCartItem(cartItem);
            cartItemRepository.save(cartItem);
            log.info("Added CartItem: {}", cartItemRepository.findById(cartItem.getId())); // Fetch and log the saved CartItem
        } else {
            // If there's an existing CartItem for the service, just increase its quantity
            log.info("Increasing quantity of existing CartItem: {}", existingCartItem);
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
            log.info("Updated CartItem: {}", cartItemRepository.findById(existingCartItem.getId())); // Fetch and log the updated CartItem
        }

        cart = cartRepository.save(cart);

        // Fetch and log the saved Cart
        Optional<Cart> savedCartOptional = cartRepository.findById(cart.getId());
        if (savedCartOptional.isPresent()) {
            log.info("Saved Cart: {}", savedCartOptional.get());
        } else {
            log.info("Failed to fetch the saved Cart");
        }

        return cart;
    }

    @Transactional
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

    @Transactional
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    @Transactional
    public Cart findCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            return null;
        }
        return cart;
    }

    @Transactional
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
}
