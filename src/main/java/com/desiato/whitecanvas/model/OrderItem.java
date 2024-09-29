package com.desiato.whitecanvas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Order order;

    @OneToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultancyProduct service;
}
