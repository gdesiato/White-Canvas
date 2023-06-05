package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
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

        double totalAmount = 0;
        for (CartItem item : cart.getCartItems()) {
            totalAmount += item.getQuantity() * item.getService().getCost();
        }

        System.out.println("===========================");
        System.out.println("Total Price: " + totalAmount);

        order.setTotalAmount(totalAmount);

        System.out.println("===========================");
        System.out.println("Order before saving: " + order);

        return orderRepository.save(order);
    }

    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}

