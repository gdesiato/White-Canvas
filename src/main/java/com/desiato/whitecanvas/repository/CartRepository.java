package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

   Optional<Cart> findUserById(Long id);
}
