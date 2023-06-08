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
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(new Supplier<ResourceNotFoundException>() {
                    @Override
                    public ResourceNotFoundException get() {
                        return new ResourceNotFoundException("Cart not found");
                    }
                });

        // Create the order from the cart
        Order order = new Order();
        order.setUser(cart.getUser());

        // Calculate total amount and save the order
        double total = 0.0;
        for (CartItem item : cart.getCartItems()) {
            // Create a new OrderItem from the CartItem
            OrderItem orderItem = new OrderItem();
            orderItem.setService(item.getService());
            orderItem.setQuantity(item.getQuantity());

            total += item.getTotalPrice();
            addItemToOrder(order, orderItem);
        }
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Update the OrderItems to reference the saved order
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        // Clear the Cart
        cartService.clearCart(cart);

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

