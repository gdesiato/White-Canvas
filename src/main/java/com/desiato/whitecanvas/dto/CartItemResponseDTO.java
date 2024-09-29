package com.desiato.whitecanvas.dto;

import com.desiato.whitecanvas.model.ConsultancyProduct;

import java.math.BigDecimal;

public record CartItemResponseDTO(ConsultancyProduct consultancyProduct, BigDecimal price, int quantity) {
}
