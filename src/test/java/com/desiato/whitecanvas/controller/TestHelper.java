package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.UserRepository;
import com.desiato.whitecanvas.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class TestHelper {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private String generateUniqueEmail() {
        return "user-" + System.currentTimeMillis() + "@example.com";
    }
}
