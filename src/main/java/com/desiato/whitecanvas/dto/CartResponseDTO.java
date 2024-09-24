package com.desiato.whitecanvas.dto;

import java.math.BigDecimal;

public record CartResponseDTO(Long id, Long userId, BigDecimal totalPrice) {
}
