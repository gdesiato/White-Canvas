package com.desiato.whitecanvas;

import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.dto.UserToken;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.AuthenticationService;
import com.desiato.whitecanvas.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class TestAuthenticationHelper {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticatedUser createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String rawPassword = "password123"; // Raw password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        log.info("Creating user with email: {}", email);
        log.info("Raw password: {}", rawPassword);
        log.info("Encoded password: {}", encodedPassword);

        // Create user with raw password, which will be encoded inside createUser
        User existingUser = userService.createUser(email, rawPassword); // Pass raw password here to be encoded inside createUser

        // Log the encoded password saved in the user object
        log.info("User saved with encoded password: {}", existingUser.getPassword());

        // Authenticate user with raw password
        AuthenticationRequestDto request = new AuthenticationRequestDto(email, rawPassword);
        UserToken userToken = authenticationService.authenticate(request);

        return new AuthenticatedUser(existingUser, userToken);
    }

    public User createAndPersistUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        return userService.createUser(email, encodedPassword);
    }

    public String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
