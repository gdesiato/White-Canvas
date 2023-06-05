package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Services;
import com.example.demo.models.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public Cart getShoppingCartForUser(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }
        return user.getCart();
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
        User user = userService.getUserById(userId);
        if (user == null) {
            return null;
        }

        Services service = serviceRepository.findByServiceName(serviceName);
        if (service == null) {
            return null;
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = createCart(userId);
        }

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
            cart.addCartItem(cartItem);
            cartItemRepository.save(cartItem);
        } else {
            // If there's an existing CartItem for the service, just increase its quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        }

        cartRepository.save(cart);

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

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    public Double getTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getService().getCost() * item.getQuantity())
                .sum();
    }

    public Cart findCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
