package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Consultancy service;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public CartItem(Consultancy service, int quantity) {
        this.service = service;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return quantity * service.getCost();
    }

    public Consultancy getService() {
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


