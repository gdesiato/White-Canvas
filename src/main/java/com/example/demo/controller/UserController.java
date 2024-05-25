package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;
    CartService cartService;

    public UserController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user = userService.createUser(user.getEmail(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserByUserId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
