package com.desiato.whitecanvas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        UserDTO userDTO,
        List<OrderItemDTO> items,
        LocalDateTime orderDate,
        BigDecimal totalAmount) {
}

