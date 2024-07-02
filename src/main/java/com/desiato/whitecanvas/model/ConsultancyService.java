package com.desiato.whitecanvas.model;

import java.math.BigDecimal;

public enum ConsultancyService {
    COLOR_ANALYSIS("Color Analysis", new BigDecimal("150.00")),
    BODY_SHAPE("Body Shape", new BigDecimal("100.00")),
    FACIAL_SHAPE("Facial Shape", new BigDecimal("300.00"));

    private final String serviceName;
    private final BigDecimal price;

    ConsultancyService(String serviceName, BigDecimal price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
