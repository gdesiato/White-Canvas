package com.desiato.whitecanvas.dto;

import java.util.List;

public record UserResponseDTO(Long id, String email, List<OrderResponseDTO> orders, CartResponseDTO cart) {
}
