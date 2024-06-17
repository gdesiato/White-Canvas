package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
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

    @Test
    public void createUserAndRetrieveUser_ShouldCreateAndReturnUser() throws Exception {
        String userJson = """
        {
            "email": "testuser@example.com",
            "password": "password123"
        }
        """;

        // Create user via POST request
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("testuser@example.com"));

        // Retrieve the created user via GET request
        mockMvc.perform(get("/api/user/1")  // Assuming the created user gets ID 1
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

}
