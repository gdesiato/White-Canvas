package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;
    private Cart testCart;

    @BeforeEach
    public void setUp() {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        testCart = authenticatedUser.user().getCart();
    }

    @Test
    void GetCartByUserId_ShouldReturnCartId() throws Exception {
        Long userId = authenticatedUser.user().getId();

        mockMvc.perform(get("/api/cart/user/{userId}", userId)
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCart.getId()));
    }

    @Test
    void addToCart_ShouldVerifyTheAddedItemsInTheCart() throws Exception {
        Long userId = authenticatedUser.user().getId();
        ConsultancyProduct consultancyProduct = ConsultancyProduct.COLOR_ANALYSIS;
        Integer quantity = 2;

        String cartRequestJson = """
                {
                    "items": [
                        {
                            "consultancyProduct": "%s",
                            "quantity": %d
                        }
                    ]
                }
                """.formatted(consultancyProduct.name(), quantity);

        // Add items to the cart
        mockMvc.perform(post("/api/cart/{userId}/addToCart", userId)
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].consultancyProduct").value("COLOR_ANALYSIS"))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }
}
