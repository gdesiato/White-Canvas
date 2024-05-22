package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/update/{id}")
    public User showUpdateUserForm(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @GetMapping("/dashboard")
    public UserDTO showUserDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        Cart userCart = cartService.findCartByUser(user);
        if (userCart == null) {
            userCart = new Cart();
            userCart.setUser(user);
            cartService.saveCart(userCart);
        }
        return new UserDTO(user, userCart.getItems());
    }

    @GetMapping("/checkout")
    public CheckoutDTO getCheckoutInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        Cart userCart = cartService.findCartByUser(user);
        return new CheckoutDTO(user, userCart.getItems());
    }
}
