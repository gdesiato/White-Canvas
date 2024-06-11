package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order-confirmation")
    public ResponseEntity<Order> showOrderConfirmation(@RequestParam("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

}
