package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

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

    public List<CartItem> getItems() {
        return new ArrayList<>(this.cartItems); // make a copy of the list to prevent modification
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this); // to establish the bidirectional relationship
    }

    public List<Consultancy> getServices() {
        return cartItems.stream()
                .map(CartItem::getService)
                .collect(Collectors.toList());
    }

    public double getTotalPrice() {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
}
