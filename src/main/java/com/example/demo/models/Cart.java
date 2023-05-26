package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Cart(User user) {
    }

    public Cart() {

    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }


}