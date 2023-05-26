package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Services;
import com.example.demo.models.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    ServicesRepository servicesRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;


    public Cart getCurrentCart(User user) {
        // Fetch the cart from the repository using the user
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = createCart(user);
        }
        return cart;
    }

    public Cart createCart(User user) {
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }

    public void addItemToCart(User user, Long serviceId, int quantity) {
        Cart cart = getCurrentCart(user);
        if(cart != null) {
            CartItem cartItem = cartItemRepository.findCartItemByIdAndCart(serviceId, cart);
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                Services service = getServiceById(serviceId);
                cartItem = new CartItem(service, quantity);
                cart.addCartItem(cartItem);
            }
        }
    }

    public void removeItemFromCart(User user, Long serviceId) {
        Cart cart = getCurrentCart(user);
        if(cart != null) {
            CartItem cartItem = findCartItemById(cart, serviceId);
            if (cartItem != null) {
                cart.getCartItems().remove(cartItem);
            }
        }
    }

    public void clearCart(User user) {
        Cart cart = getCurrentCart(user);
        if(cart != null) {
            cart.getCartItems().clear();
        }
    }

    private CartItem findCartItemById(Cart cart, Long serviceId) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getService().getId().equals(serviceId)) {
                return cartItem;
            }
        }
        return null;
    }

    private Services getServiceById(Long serviceId) {
        return servicesRepository.findById(serviceId).orElse(null);
    }
}
