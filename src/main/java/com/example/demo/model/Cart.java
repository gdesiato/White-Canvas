package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> cartItems;


    private BigDecimal totalPrice;  // Use BigDecimal for monetary values

    public void recalculateTotalPrice() {
        BigDecimal total = cartItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        setTotalPrice(total);
    }

    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    public Cart(User user) {
        this.user = user;
        this.cartItems = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(this.cartItems);
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
}
