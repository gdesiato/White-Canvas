package com.desiato.whitecanvas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long userId,
        List<OrderItemDTO> items,
        LocalDateTime orderDate,
        BigDecimal totalAmount) {
}

