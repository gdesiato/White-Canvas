package com.desiato.whitecanvas.dto;

import java.math.BigDecimal;

public record OrderItemDTO(Long id, String service, int quantity, BigDecimal price) {
}
