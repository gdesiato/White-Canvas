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

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order addItemToOrder(Order order, OrderItem orderItem) {
        order.getItems().add(orderItem);
        orderItem.setOrder(order);
        return order;
    }
}

