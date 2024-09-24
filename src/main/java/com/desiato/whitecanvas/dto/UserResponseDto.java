package com.desiato.whitecanvas.dto;

import java.util.List;

public record UserResponseDto(Long id, String email, List<OrderResponseDTO> orders, CartResponseDTO cart) {
}
