package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.repository.UserRepository;
import com.desiato.whitecanvas.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@SpringBootTest
@AutoConfigureMockMvc
public class TestHelper {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private String generateUniqueEmail() {
        return "user-" + System.currentTimeMillis() + "@example.com";
    }
}
