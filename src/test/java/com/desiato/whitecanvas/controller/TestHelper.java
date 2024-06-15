package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class TestHelper {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);

        String token = authenticationService.authenticate(newUser);
        return token;
    }

    /**
     * Generates a unique email. This should be implemented to ensure uniqueness.
     * @return a unique email as a String.
     */
    private String generateUniqueEmail() {
        // Implement this method to generate unique email addresses
        return "user-" + System.currentTimeMillis() + "@example.com";
    }

    private String generateUniqueEmail() {
        return "user-" + System.currentTimeMillis() + "@example.com";
    }
}
}
