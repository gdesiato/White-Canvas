package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.UserRequestDto;
import com.desiato.whitecanvas.exception.UserAlreadyExistsException;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final SessionService sessionService;


    @Transactional
    public User createUser(String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);

        Cart cart = new Cart();
        cartService.saveCart(cart); // Save the cart first

        // Associate the cart with the user and save the user
        newUser.setCart(cart);
        saveUser(newUser);

        return newUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        sessionService.deleteUserSessions(id);
        userRepository.deleteById(id);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    public User updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userRequestDto.email() != null) {
            existingUser.setEmail(userRequestDto.email());
        }
        if (userRequestDto.password() != null && !userRequestDto.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.password()));
        }

        return userRepository.save(existingUser);
    }
}
