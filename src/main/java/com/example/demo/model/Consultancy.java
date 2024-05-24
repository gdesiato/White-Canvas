package com.example.demo.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Consultancy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String consultancyName;
    private String description;
    private Double cost;
    private String image;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    public Long getId() {
        return id;
    }

}
