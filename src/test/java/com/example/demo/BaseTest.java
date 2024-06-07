package com.example.demo;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BaseTest {

    private final MockMvc mockMvc;

    public BaseTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

}
