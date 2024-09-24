package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.repository.*;
import com.desiato.whitecanvas.model.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        return order;
    }

    public Order addItemToOrder(Order order, OrderItem orderItem) {
        order.getItems().add(orderItem);
        orderItem.setOrder(order);
        return order;
    }
}

