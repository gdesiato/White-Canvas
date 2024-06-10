package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItermRepository extends JpaRepository<CartItem, Long> {
}
