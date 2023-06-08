package com.example.demo.repositories;

import com.example.demo.models.Cart;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
}