package com.desiato.whitecanvas.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum ConsultancyProduct {
    COLOR_ANALYSIS("Color Analysis", new BigDecimal("150.00")),
    BODY_SHAPE("Body Shape", new BigDecimal("100.00")),
    FACIAL_SHAPE("Facial Shape", new BigDecimal("100.00"));

    private final String serviceName;

    private final BigDecimal price;

    ConsultancyProduct(String serviceName, BigDecimal price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public static ConsultancyProduct fromServiceName(String serviceName) {
        for (ConsultancyProduct product : values()) {
            if (product.getServiceName().equalsIgnoreCase(serviceName)) {
                return product;
            }
        }
        throw new IllegalArgumentException("Service not found: " + serviceName);
    }
}
