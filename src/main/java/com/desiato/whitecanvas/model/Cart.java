package com.desiato.whitecanvas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @OneToOne(mappedBy = "cart")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);  // Set the bidirectional relationship
        updateTotalPrice();
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            totalPrice = totalPrice.add(item.getTotalPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
    }
}
