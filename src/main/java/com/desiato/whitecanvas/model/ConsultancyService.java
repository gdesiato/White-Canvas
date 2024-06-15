package com.desiato.whitecanvas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "services")
public class ConsultancyService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Double cost;
    private String image;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    public Long getId() {
        return id;
    }
}
