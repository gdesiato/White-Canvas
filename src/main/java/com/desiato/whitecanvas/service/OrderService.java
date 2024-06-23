package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.repository.*;
import com.desiato.whitecanvas.model.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItermRepository cartItemRepository;

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order addItemToOrder(Order order, OrderItem orderItem) {
        order.getOrderItems().add(orderItem);
        orderItem.setOrder(order);
        return order;
    }
}

