package com.example.demo.repositories;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Finds a CartItem by its id that belongs to a specific Cart
    CartItem findCartItemByIdAndCart(Long id, Cart cart);
}
