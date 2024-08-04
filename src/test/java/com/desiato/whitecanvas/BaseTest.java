package com.desiato.whitecanvas;

import com.desiato.whitecanvas.repository.*;
import com.desiato.whitecanvas.service.AuthenticationService;
import com.desiato.whitecanvas.service.OrderService;
import com.desiato.whitecanvas.service.SessionService;
import com.desiato.whitecanvas.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AllArgsConstructor
@AutoConfigureMockMvc
public class BaseTest {

    private final MockMvc mockMvc;
    private final SessionService sessionService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final TestAuthenticationHelper testAuthenticationHelper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

}
