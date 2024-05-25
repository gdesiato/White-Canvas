package com.example.demo.controller;

import com.example.demo.service.CartService;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private CartService cartService;
    private OrderService orderService;
    private UserService userService;

    public OrderController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/order-confirmation")
    public ResponseEntity<Order> showOrderConfirmation(@RequestParam("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

}
