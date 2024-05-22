package com.example.demo.model;

import java.util.List;

public record UserDTO(User user, List<CartItem> items) {
}
