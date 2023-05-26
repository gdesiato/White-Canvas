package com.example.demo.models;


import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String serviceName;
    private String description;
    private Double cost;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

}
