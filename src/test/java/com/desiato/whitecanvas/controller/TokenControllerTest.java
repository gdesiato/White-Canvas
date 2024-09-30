package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TokenControllerTest extends BaseTest {

    @Test
    void authenticateAndGenerateToken_ShouldReturnToken() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        String email = authenticatedUser.user().getEmail();
        String password = "password123";

        String authenticationRequestJson = String.format("""
            {\s
                "email": "%s",
                "password": "%s"\s
            }
            """, email, password);

        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(authenticatedUser.userToken().value()));
    }
}
