package com.desiato.whitecanvas;

import com.desiato.whitecanvas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public SessionRepository sessionRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TestAuthenticationHelper testAuthenticationHelper;

}
