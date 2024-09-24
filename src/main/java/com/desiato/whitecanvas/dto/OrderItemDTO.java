package com.desiato.whitecanvas.dto;

import com.desiato.whitecanvas.model.ConsultancyProduct;
import com.desiato.whitecanvas.model.Order;

import java.math.BigDecimal;

public record OrderItemDTO(Long id, ConsultancyProduct service, int quantity, Order order) {
}
