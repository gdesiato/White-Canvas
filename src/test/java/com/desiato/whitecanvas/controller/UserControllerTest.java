package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends BaseTest {


    @Test
    public void getUser_ShouldReturnUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authenticatedUser.user().getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUser.user().getEmail()));
    }

    @Test
    public void createUser_ShouldHandleUserAlreadyExists() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        String userJson = String.format("""
                { 
                    "email": "%s",
                    "password": "password123" 
                }
                """,
                authenticatedUser.user().getEmail());

        // First attempt should succeed
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Second attempt with the same email should fail with 409 Conflict
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }

    @Test
    public void createAndAuthenticateUser_ShouldReturnAuthenticatedUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        assertNotNull(authenticatedUser);
        assertNotNull(authenticatedUser.user());
        assertNotNull(authenticatedUser.userToken());
        assertEquals("user_", authenticatedUser.user().getEmail().substring(0, 5));
    }

}
