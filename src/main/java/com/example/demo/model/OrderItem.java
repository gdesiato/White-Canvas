package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Consultancy service;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem(){}

    public OrderItem(Consultancy service, int quantity, Order order) {
        this.service = service;
        this.quantity = quantity;
        this.order = order;
    }

    public double getTotalPrice() {
        return quantity * service.getCost();
    }
}
