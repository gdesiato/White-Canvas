package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.ServicesService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @MockBean
    private ServicesService servicesService;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;



    @Test
    @WithMockUser(username = "testUser")
    public void testGetCart() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUser");

        Cart testCart = new Cart();
        testCart.setId(1L);
        testCart.setUser(testUser);

        given(userService.findByUsername("testUser")).willReturn(testUser);
        given(cartService.getShoppingCartForUser("testUser")).willReturn(testCart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {"USER"})
    public void testRemoveItemFromCart() throws Exception {
        Long testItemId = 1L;
        String username = "testUser";

        User testUser = new User();
        testUser.setUsername(username);
        testUser.setId(1L);

        Cart testCart = new Cart();
        testCart.setId(1L);

        CartItem testCartItem = new CartItem();
        testCartItem.setId(testItemId);

        given(userService.findByUsername(username)).willReturn(testUser);
        given(cartService.getShoppingCartForUser(username)).willReturn(testCart);
        given(cartItemRepository.findById(testItemId)).willReturn(Optional.of(testCartItem));

    }


}

