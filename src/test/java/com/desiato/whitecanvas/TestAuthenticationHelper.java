package com.desiato.whitecanvas;

import com.desiato.whitecanvas.dto.*;
import com.desiato.whitecanvas.model.*;
import com.desiato.whitecanvas.repository.CartItemRepository;
import com.desiato.whitecanvas.service.AuthenticationService;
import com.desiato.whitecanvas.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TestAuthenticationHelper {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final CartItemRepository cartItemRepository;

    public AuthenticatedUser createAndAuthenticateUser() {
        String email = generateUniqueEmail();
        String rawPassword = "password123";

        User newUser = userService.createUser(email, rawPassword);

        AuthenticationRequestDto request = new AuthenticationRequestDto(email, rawPassword);

        String userToken = authenticationService.authenticateAndGenerateToken(request);

        return new AuthenticatedUser(newUser, new UserToken(userToken));
    }

    public User createAndPersistUser() {
        String email = generateUniqueEmail();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        return userService.createUser(email, encodedPassword);
    }

    public CartItem createCartItem(Cart cart, ConsultancyProduct service, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setService(service);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public OrderItem createOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setCartItem(cartItem);
        orderItem.setService(cartItem.getService());
        return orderItem;
    }

    public String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
