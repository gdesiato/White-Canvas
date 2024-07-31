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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends BaseTest {

    private Order testOrder;
    private AuthenticatedUser authenticatedUser;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    public void setUp() throws Exception {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        // Create CartItems
        cartItem1 = new CartItem();
        cartItem1.setCart(authenticatedUser.user().getCart());
        cartItem1.setService(ConsultancyProduct.COLOR_ANALYSIS);
        cartItem1.setQuantity(1);
        cartItem1 = cartItemRepository.save(cartItem1);

        cartItem2 = new CartItem();
        cartItem2.setCart(authenticatedUser.user().getCart());
        cartItem2.setService(ConsultancyProduct.BODY_SHAPE);
        cartItem2.setQuantity(2);
        cartItem2 = cartItemRepository.save(cartItem2);

        // Create Order
        testOrder = new Order();
        testOrder.setUser(authenticatedUser.user());
        testOrder.setItems(new ArrayList<>());

        // Create OrderItems
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder(testOrder);
        orderItem1.setCartItem(cartItem1);
        orderItem1.setService(cartItem1.getService());
        testOrder.getItems().add(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(testOrder);
        orderItem2.setCartItem(cartItem2);
        orderItem2.setService(cartItem2.getService());
        testOrder.getItems().add(orderItem2);

        // Set Order Details
        testOrder.setOrderDate(LocalDateTime.now());

        BigDecimal price1 = cartItem1.getService().getPrice();
        BigDecimal price2 = cartItem2.getService().getPrice();

        BigDecimal totalAmount = price1.multiply(BigDecimal.valueOf(cartItem1.getQuantity()))
                .add(price2.multiply(BigDecimal.valueOf(cartItem2.getQuantity())));

        testOrder.setTotalAmount(totalAmount);

        testOrder = orderRepository.save(testOrder);

        // Verify Order Creation
        assertNotNull(testOrder.getId());
        assertNotNull(testOrder.getUser());
        assertEquals(authenticatedUser.user().getId(), testOrder.getUser().getId());
        assertEquals(totalAmount, testOrder.getTotalAmount());
    }

    @Test
    public void showOrderConfirmation_ShouldReturnOrder() throws Exception {
        mockMvc.perform(get("/api/order/order-confirmation")
                        .header("authToken", authenticatedUser.userToken().value())
                        .param("orderId", testOrder.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId()))
                .andExpect(jsonPath("$.userDTO.id").value(testOrder.getUser().getId()))
                .andExpect(jsonPath("$.userDTO.email").value(testOrder.getUser().getEmail()))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].service").value("COLOR_ANALYSIS"))
                .andExpect(jsonPath("$.items[0].quantity").value(1))
                .andExpect(jsonPath("$.items[0].price").value("150.0")) // review. should be 150.00
                .andExpect(jsonPath("$.items[1].service").value("BODY_SHAPE"))
                .andExpect(jsonPath("$.items[1].quantity").value(2))
                .andExpect(jsonPath("$.items[1].price").value("200.0")) //review. should be 200.00
                .andExpect(jsonPath("$.orderDate").exists())
                .andExpect(jsonPath("$.totalAmount").value("350.0"));// should be 350.00
    }

    @Test
    public void showOrderConfirmation_OrderNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/order/order-confirmation")
                        .header("authToken", authenticatedUser.userToken().value())
                        .param("orderId", "9999")  // not existing ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
