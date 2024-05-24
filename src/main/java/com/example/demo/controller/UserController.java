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

    UserService userService;
    CartService cartService;

    public UserController(UserService userService, CartService cartService){
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserByUserId(id);
    }

    @GetMapping("/update/{id}")
    public User showUpdateUserForm(@PathVariable("id") Long id) {
        return userService.getUserByUserId(id);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

}
