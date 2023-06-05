package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.models.Order;
import com.example.demo.services.CartService;
import com.example.demo.services.OrderService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createOrder() {
        String username = userService.getCurrentUsername();
        if (username == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getShoppingCartForUser(username);
        if (cart == null || cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        Order order = orderService.createOrderFromCart(cart);
        cartService.clearCart(cart);

        return "redirect:/order-confirmation";
    }
}

