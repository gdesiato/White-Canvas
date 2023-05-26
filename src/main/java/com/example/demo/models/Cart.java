package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<CartItem> cartItems;
    private CartItem[] items;


    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    public Cart(User user) {
        this.user = user;
        this.cartItems = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }


    public List<Services> getServices() {
        return cartItems.stream()
                .map(CartItem::getService)
                .collect(Collectors.toList());
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        if (this.items != null) {
            for (CartItem item : this.items) {
                totalPrice += item.getTotalPrice();
            }
        }
        return totalPrice;
    }
}