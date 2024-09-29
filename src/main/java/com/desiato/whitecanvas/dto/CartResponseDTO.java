package com.desiato.whitecanvas.dto;

import java.math.BigDecimal;
import java.util.List;


public record CartResponseDTO(Long id, Long userId, List<CartItemResponseDTO> items, BigDecimal totalPrice) {
}
