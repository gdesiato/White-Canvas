package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Services;
import com.example.demo.models.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

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
        userService.saveUser(user);  // Save the User entity
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
        CartItem cartItem = new CartItem(service, quantity);
        cartItem.setCart(cart);
        cart.addCartItem(cartItem);
        cartRepository.save(cart); // save the Cart in the repository
        cartItemRepository.save(cartItem); // save the CartItem in the repository
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
