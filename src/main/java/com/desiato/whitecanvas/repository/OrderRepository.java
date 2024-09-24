package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<User> findUserByOrderId(Long orderId);
}
