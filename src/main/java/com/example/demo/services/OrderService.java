package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Order;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
            total += item.getService().getCost() * item.getQuantity();
            order.addItem(item);  // Add this line. Also, make sure to create the addItem method in Order class
        }
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Update the CartItems to reference the saved order
        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.setOrder(savedOrder);
            cartItemRepository.save(cartItem);
        }

        return savedOrder;
    }

    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}

