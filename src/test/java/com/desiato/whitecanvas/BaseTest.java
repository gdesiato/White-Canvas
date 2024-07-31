package com.desiato.whitecanvas;

import com.desiato.whitecanvas.repository.*;
import com.desiato.whitecanvas.service.AuthenticationService;
import com.desiato.whitecanvas.service.OrderService;
import com.desiato.whitecanvas.service.SessionService;
import com.desiato.whitecanvas.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public SessionService sessionService;

    @Autowired
    public UserService userService;

    @Autowired
    public OrderService orderService;

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public SessionRepository sessionRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CartItemRepository cartItemRepository;

    @Autowired
    public CartRepository cartRepository;

    @Autowired
    public TestAuthenticationHelper testAuthenticationHelper;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService authenticationService;

    @Autowired
    public ObjectMapper objectMapper;

}
