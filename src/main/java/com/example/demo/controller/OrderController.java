package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private CartService cartService;
    private OrderService orderService;
    private UserService userService;
    private EntityManager entityManager;

    public OrderController(CartService cartService, OrderService orderService, UserService userService, EntityManager entityManager) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
        this.entityManager = entityManager;
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
