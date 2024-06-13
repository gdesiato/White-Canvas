package com.example.demo.controller;

import com.example.demo.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends BaseTest {

    @Test
    public void createUser_ShouldCreateUserAndReturnCreatedUser() throws Exception {
        String userJson = """
        {
            "email": "testuser@example.com",
            "password": "password123"
        }
        """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andReturn();
    }


}
