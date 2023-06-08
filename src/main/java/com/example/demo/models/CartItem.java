package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public CartItem(Services service, int quantity) {
        this.service = service;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return quantity * service.getCost();
    }

    public Services getService() {
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


