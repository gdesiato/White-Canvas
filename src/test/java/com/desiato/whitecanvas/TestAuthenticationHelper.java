package com.desiato.whitecanvas;

import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.dto.UserToken;
import com.desiato.whitecanvas.model.*;
import com.desiato.whitecanvas.repository.CartItemRepository;
import com.desiato.whitecanvas.repository.UserRepository;
import com.desiato.whitecanvas.service.AuthenticationService;
import com.desiato.whitecanvas.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    public AuthenticatedUser createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String rawPassword = "password123"; // Raw password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create user with raw password, which will be encoded inside createUser
        User existingUser = userService.createUser(email, rawPassword);

        // Authenticate user with raw password
        AuthenticationRequestDto request = new AuthenticationRequestDto(email, rawPassword);
        UserToken userToken = authenticationService.authenticate(request);

        return new AuthenticatedUser(existingUser, userToken);
    }

    public User createAndPersistUser() throws Exception {
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
