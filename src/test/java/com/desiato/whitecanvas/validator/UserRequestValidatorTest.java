package com.desiato.whitecanvas.validator;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRequestValidatorTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenPasswordIsBlank() throws Exception {
        String email = testAuthenticationHelper.generateUniqueEmail();

        String userRequestJson = String.format("""
        {
          "email": "%s",
          "password": ""
        }
        """, email);

        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Password cannot be blank"));
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenEmailIsBlank() throws Exception {

        String userRequestJson = ("""
        {
          "email": "",
          "password": "password123"
        }
        """);

        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Email cannot be blank"));
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenEmailIsInvalid() throws Exception {
        String email = "incorrectMail.com";

        String userRequestJson = String.format("""
        {
          "email": "%s",
          "password": "password123"
        }
        """, email);

        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Invalid email format"));
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenEmailAndPasswordAreBlank() throws Exception {

        String userRequestJson = ("""
        {
          "email": "",
          "password": ""
        }
        """);

        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Email cannot be blank"))
                .andExpect(jsonPath("$.errors[1].message").value("Password cannot be blank"));
    }

}
