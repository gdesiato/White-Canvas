package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem createCartItem(String serviceName, Integer quantity, Cart cart, User user) {
        ConsultancyProduct service = ConsultancyProduct.valueOf(serviceName.toUpperCase());
        CartItem cartItem = new CartItem(service, cart, user, quantity);
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
