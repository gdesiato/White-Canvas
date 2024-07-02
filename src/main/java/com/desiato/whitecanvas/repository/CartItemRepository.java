package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
}
