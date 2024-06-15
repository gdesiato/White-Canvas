package com.desiato.whitecanvas.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ConsultancyService service;

    private int quantity;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CartItem(){}

    public CartItem(ConsultancyService service, int quantity) {
        this.service = service;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return quantity * service.getCost();
    }

    public ConsultancyService getService() {
        return service;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", service=" + service +
                ", quantity=" + quantity +
                ", cart=" + cart +
                ", user=" + user +
                '}';
    }
}


