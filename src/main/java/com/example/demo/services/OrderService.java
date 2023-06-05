package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.Order;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order createOrderFromCart(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setItems(new HashSet<>(cart.getCartItems()));
        order.setTotalAmount(cartService.getTotalPrice(cart));
        return orderRepository.save(order);
    }
}

