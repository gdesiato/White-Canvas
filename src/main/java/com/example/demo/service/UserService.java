package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.CartRepository;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import org.slf4j.Logger;


@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;
    private CartService cartService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, CartRepository cartRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUserId(Long userId) throws EntityNotFoundException {
        User user = userRepository.getById(userId);
        return (User) Hibernate.unproxy(user);
    }

    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }

    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());

        // Update the password only if it has been changed
        if (!existingUser.getPassword().equals(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }

}

