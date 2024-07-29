package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.model.Session;
import com.desiato.whitecanvas.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    public void testGetUsers_ShouldReturnListOfUsers() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        mockMvc.perform(get("/api/user/list")
                        .header("authToken", authenticatedUser.userToken().value())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        String email = testAuthenticationHelper.generateUniqueEmail();
        String password = "password123";

        String userJson = String.format("""
                { 
                    "email": "%s",
                    "password": "%s" 
                }
                """, email, password);

        mockMvc.perform(post("/api/user")
                        .header("authToken", authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void createAndAuthenticateUser_ShouldReturnAuthenticatedUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        assertNotNull(authenticatedUser);
        assertNotNull(authenticatedUser.user());
        assertNotNull(authenticatedUser.userToken());
        assertEquals("user_", authenticatedUser.user().getEmail().substring(0, 5));
    }

    @Test
    public void deleteUser_ShouldDeleteUserAndReturnNoContent() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        AuthenticatedUser authenticatedUser2 = testAuthenticationHelper.createAndAuthenticateUser();

        mockMvc.perform(delete("/api/user/" + authenticatedUser2.user().getId())
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userRepository.findById(authenticatedUser2.user().getId());
        Optional<Session> deletedSession = sessionRepository.findByToken(authenticatedUser2.userToken().value());

        assertThat(deletedUser.isPresent()).isFalse();
        assertThat(deletedSession.isPresent()).isFalse();
    }

    @Test
    public void updateUser_ShouldUpdateUserAndReturnUpdatedInfo() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        AuthenticatedUser authenticatedUser2 = testAuthenticationHelper.createAndAuthenticateUser();

        String email = testAuthenticationHelper.generateUniqueEmail();

        String userJson = String.format("""
            { 
                "email": "%s"
            }
            """, email);

        mockMvc.perform(put("/api/user/update/{id}", authenticatedUser2.user().getId())
                        .header("authToken", authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }
}
