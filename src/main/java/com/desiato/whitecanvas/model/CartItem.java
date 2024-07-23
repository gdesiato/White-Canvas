package com.desiato.whitecanvas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultancyProduct service;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int quantity;

    public CartItem() {
    }

    public CartItem(ConsultancyProduct service, Integer quantity) {
        this.service = service;
        this.quantity = quantity;
    }

    public CartItem(ConsultancyProduct service, Cart cart, User user, Integer quantity) {
        this.service = service;
        this.cart = cart;
        this.user = user;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return service.getPrice().multiply(new BigDecimal(quantity));
    }
}
