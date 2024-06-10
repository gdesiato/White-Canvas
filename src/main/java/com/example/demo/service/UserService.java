package com.example.demo.service;

import com.example.demo.repository.CartRepository;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final CartService cartService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public User createUser(String email, String password) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return newUser;
    }

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

    public User getUserByUserId(Long userId) throws EntityNotFoundException {
        User user = userRepository.getById(userId);
        return user;
    }

    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());

        // Check if the password has changed, and update if necessary
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }
}
