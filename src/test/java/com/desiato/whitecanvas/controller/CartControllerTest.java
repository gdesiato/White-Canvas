package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;
    private Cart testCart;

    @BeforeEach
    public void setUp() throws Exception {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        testCart = authenticatedUser.user().getCart();
    }

    @Test
    public void GetCartByUserId_ShouldReturnCartId() throws Exception {
        Long userId = authenticatedUser.user().getId();

        mockMvc.perform(get("/api/cart/user/{userId}", userId)
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCart.getId()));
    }

    @Test
    public void addToCart_ShouldVerifyTheAddedItemsInTheCart() throws Exception {
        Long userId = authenticatedUser.user().getId();
        String consultancyName = ConsultancyProduct.COLOR_ANALYSIS.getServiceName();
        Integer quantity = 2;

        // Ensure the cart is empty before adding items
        mockMvc.perform(delete("/api/cart/{userId}/emptyCart", userId)
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk());

        // Add items to the cart
        mockMvc.perform(post("/api/cart/{userId}/addToCart", userId)
                        .header("authToken", authenticatedUser.userToken().value())
                        .param("consultancyName", consultancyName)
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].service").value(ConsultancyProduct.COLOR_ANALYSIS.name()))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(quantity));

        // Verify that the items were added with the correct quantity
        mockMvc.perform(get("/api/cart/user/{userId}", userId)
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].quantity").value(quantity));
    }

    @Test
    public void testEmptyCart() throws Exception {
        Long userId = authenticatedUser.user().getId();

        // Add an item to the cart first
        String consultancyName = ConsultancyProduct.COLOR_ANALYSIS.getServiceName();
        Integer quantity = 2;

        mockMvc.perform(post("/api/cart/{userId}/addToCart", userId)
                        .header("authToken", authenticatedUser.userToken().value())
                        .param("consultancyName", consultancyName)
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Now empty the cart
        mockMvc.perform(delete("/api/cart/{userId}/emptyCart", userId)
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk());

        // Verify the cart is empty
        MvcResult result = mockMvc.perform(get("/api/cart/user/{userId}", userId)
                        .header("authToken", authenticatedUser.userToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isEmpty())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("Response: " + content);
    }
}
