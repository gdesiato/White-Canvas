package com.example.demo.model;

import java.util.List;

public record CheckoutDTO(User user, List<CartItem> items) {
}
