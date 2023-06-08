package com.example.demo.test;

import com.example.demo.models.Cart;
import com.example.demo.models.Order;
import com.example.demo.models.Services;
import com.example.demo.models.User;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.OrderService;
import com.example.demo.services.ServicesService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    public void testPrintUserRoles() {
        // use the username of an existing user
        System.out.println("///////////////////////////////////////////////////////");
        System.out.println("///////////////////////////////////////////////////////");
        userService.printUserRoles("rr");
    }

    @Test
    public void testOrderSubmission() {
        // Create test user and save to the database
        User user = new User();
        user.setUsername("TestUsername7");
        user.setPassword("TestPassword");
        userRepository.save(user);

        // Make sure that the user is saved
        User savedUser = userRepository.findByUsername("TestUsername7");
        assertNotNull(savedUser);

        // Create a service
        Services service = new Services();
        service.setServiceName("TestService7");
        service.setCost(100.0);
        serviceRepository.save(service);

        // Make sure that the service is saved
        Services savedService = serviceRepository.findByServiceName("TestService7");
        assertNotNull(savedService);

        // Simulate adding items to a cart
        Integer quantity = 2; // Set your desired quantity
        Cart cart = cartService.addToCart(savedUser.getId(), savedService.getServiceName(), quantity);
        assertNotNull(cart);
        assertFalse(cart.getCartItems().isEmpty());

        // Simulate order submission
        Order order = orderService.createOrderFromCart(cart.getId());
        assertNotNull(order);
        assertFalse(order.getItems().isEmpty());

        // Check if the order items match the cart items
        assertEquals(cart.getCartItems().size(), order.getItems().size());
    }

}
