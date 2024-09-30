package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.dto.ConsultancyProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConsultancyControllerTest extends BaseTest {

    private String authToken;

    @BeforeEach
    void setup() {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        authToken = authenticatedUser.userToken().value();
    }

    @Test
    void getAllConsultancyServices_ShouldReturnListOfServices() throws Exception {

        mockMvc.perform(get("/api/consultancy")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].serviceName").value("Color Analysis"))
                .andExpect(jsonPath("$[0].price").value(150.00))
                .andExpect(jsonPath("$[1].serviceName").value("Body Shape"))
                .andExpect(jsonPath("$[1].price").value(100.00))
                .andExpect(jsonPath("$[2].serviceName").value("Facial Shape"))
                .andExpect(jsonPath("$[2].price").value(100.00));
    }
}
