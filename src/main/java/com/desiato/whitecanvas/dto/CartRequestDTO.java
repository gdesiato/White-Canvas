package com.desiato.whitecanvas.dto;

import java.util.List;

public record CartRequestDTO(List<CartItemRequestDTO> items) {
}
