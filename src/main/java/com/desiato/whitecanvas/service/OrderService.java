package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.DtoMapper;
import com.desiato.whitecanvas.dto.OrderRequestDTO;
import com.desiato.whitecanvas.exception.OrderNotFoundException;
import com.desiato.whitecanvas.exception.UserNotFoundException;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.*;
import com.desiato.whitecanvas.model.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order with id: " + orderId + " does not exist."));

    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .toList();
    }

    public void deleteOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            orderRepository.deleteById(orderId);
        } throw new OrderNotFoundException("Order with id: " + orderId + "was not found");
    }

    public Order updateOrder(Long orderId, OrderRequestDTO orderRequestDto) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> {
                    User user = userRepository.findById(orderRequestDto.userId())
                            .orElseThrow(() ->
                                    new UserNotFoundException("User with id: " + orderRequestDto.userId() + " not found"));
                    existingOrder.setUser(user);
                    List<OrderItem> orderItems = orderRequestDto.items().stream()
                            .map(dtoMapper::toOrderItem)
                            .toList();
                    existingOrder.setItems(orderItems);

                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " was not found"));
    }
}

