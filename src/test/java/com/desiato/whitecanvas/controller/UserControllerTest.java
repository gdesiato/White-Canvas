package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
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
    public void getUser_ShouldReturnUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authenticatedUser.user().getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUser.user().getEmail()));
    }

}
