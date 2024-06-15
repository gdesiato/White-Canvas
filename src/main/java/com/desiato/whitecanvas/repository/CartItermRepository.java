package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItermRepository extends JpaRepository<CartItem, Long> {
}
