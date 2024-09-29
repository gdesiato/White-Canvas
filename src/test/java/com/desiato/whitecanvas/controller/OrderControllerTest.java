package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends BaseTest {

    private Order testOrder;
    private AuthenticatedUser authenticatedUser;


    @BeforeEach
    public void setUp() {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        // Create CartItems
        CartItem cartItem1 = testAuthenticationHelper.createCartItem(
                authenticatedUser.user().getCart(),
                ConsultancyProduct.COLOR_ANALYSIS, 1);
        CartItem cartItem2 = testAuthenticationHelper.createCartItem(
                authenticatedUser.user().getCart(),
                ConsultancyProduct.BODY_SHAPE, 2);

        // Create Order
        testOrder = new Order();
        testOrder.setUser(authenticatedUser.user());
        testOrder.setItems(new ArrayList<>());

        // Create OrderItems
        OrderItem orderItem1 = testAuthenticationHelper.createOrderItem(testOrder, cartItem1);
        OrderItem orderItem2 = testAuthenticationHelper.createOrderItem(testOrder, cartItem2);

        testOrder.getItems().add(orderItem1);
        testOrder.getItems().add(orderItem2);

        testOrder.setOrderDate(LocalDateTime.now());

        BigDecimal totalAmount = cartItem1.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem1.getQuantity()))
                .add(cartItem2.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem2.getQuantity())));

        testOrder.setTotalAmount(totalAmount);

        testOrder = orderRepository.save(testOrder);
    }

    @Test
    void shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get("/api/order")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        mockMvc.perform(get("/api/order/1")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.00));
    }

    @Test
    public void shouldReturnAllOrdersWithoutSpecifyingIds() throws Exception {
        mockMvc.perform(get("/api/order")
                        .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber())  // Check that the response is an array of orders
                .andExpect(jsonPath("$[0]").exists())  // Check that at least one order exists
                .andExpect(jsonPath("$[0].totalAmount").isNumber())  // Ensure the totalAmount field is a number
                .andExpect(jsonPath("$[*].id").exists())  // Ensure each order has an ID
                .andExpect(jsonPath("$[*].totalAmount").isNotEmpty())  // Ensure all orders have totalAmount field
                .andExpect(jsonPath("$[*].orderDate").isNotEmpty());  // Ensure all orders have an orderDate field
    }

    @Test
    void getOrderById_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/order/9999")
                .header("Authorization", "Bearer " + authenticatedUser.userToken().value())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
