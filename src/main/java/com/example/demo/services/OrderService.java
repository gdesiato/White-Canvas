package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Transactional
    public Order createOrderFromCart(Long cartId) {
        final Logger log = LogManager.getLogger(OrderService.class);

        System.out.println("Service Method CreateOrderFromCart triggered");

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("Cart not found");
                    return new ResourceNotFoundException("Cart not found");
                });

        log.info("Creating order from cart with ID: {}", cart.getId());
        log.info("Cart retrieved: {}", cart);

        // Create the order from the cart
        Order order = new Order();
        order.setUser(cart.getUser());

        // Calculate total amount and save the order
        double total = 0.0;
        if (cart.getCartItems().isEmpty()) {
            log.info("CartItems is empty");
        } else {
            for (CartItem item : cart.getCartItems()) {
                log.info("Processing cart item: {}", item);
                // Create a new OrderItem from the CartItem
                OrderItem orderItem = new OrderItem();
                orderItem.setService(item.getService());
                orderItem.setQuantity(item.getQuantity());

                total += item.getTotalPrice();
                addItemToOrder(order, orderItem);
                log.info("Added item to order: {}", orderItem);
            }
        }
        order.setTotalAmount(total);

        log.info("Saving order to the database");

        Order savedOrder = orderRepository.save(order);

        log.info("Saved order: {}", savedOrder);

        // Update the OrderItems to reference the saved order
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        // Instead of clearing the Cart, remove the items from the Cart.
        // This will reuse the same Cart for the next order.
        List<CartItem> items = new ArrayList<>(cart.getCartItems());
        for (CartItem item : items) {
            cartService.removeItemFromCart(cart, item);
        }


        log.info("Order created successfully. Order ID: {}", savedOrder.getId());

        return savedOrder;
    }



    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    @Transactional
    public Order addItemToOrder(Order order, OrderItem orderItem) {
        order.getOrderItems().add(orderItem);
        orderItem.setOrder(order);
        return order;
    }
}

