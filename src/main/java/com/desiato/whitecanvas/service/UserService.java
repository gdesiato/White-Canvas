package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.CartDTO;
import com.desiato.whitecanvas.dto.UserDTO;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.CartRepository;
import com.desiato.whitecanvas.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Transactional
    public User createUser(String email, String password) {
        log.info("Encoding password for email: {}", email);
        String encodedPassword = passwordEncoder.encode(password);

        // Create and save the user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword); // Set the encoded password
        userRepository.save(newUser);

        // Log the encoded password after saving
        log.info("User saved with encoded password: {}", newUser.getPassword());

        // Create and save the cart associated with the user
        Cart cart = new Cart();
        newUser.setCart(cart);
        cartRepository.save(cart);

        newUser.setCart(cart);
        userRepository.save(newUser);

        return newUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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

    public Optional<User> getUserById(Long userId) throws EntityNotFoundException {
        User user = userRepository.getById(userId);
        return Optional.of(user);
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
