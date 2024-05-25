package com.example.demo.service;

import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private UserRepository userRepository;
    private CartService cartService;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public OrderService(UserRepository userRepository, CartService cartService, OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order addItemToOrder(Order order, OrderItem orderItem) {
        order.getOrderItems().add(orderItem);
        orderItem.setOrder(order);
        return order;
    }
}

