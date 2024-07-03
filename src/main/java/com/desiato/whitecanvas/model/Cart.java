package com.desiato.whitecanvas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    private BigDecimal totalPrice;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        updateTotalPrice();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            totalPrice = totalPrice.add(item.getTotalPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
    }
}
