package com.dinendrop.restaurantservice.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Restaurant {

    @Id
    private String id;

    private String name;
    private String areaCode;
    private String city;
    private LocalTime openAt;
    private LocalTime closeAt;

    private boolean online = true;

    @Column(name = "dining_enabled")
    private boolean diningEnabled = true;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menu = new ArrayList<>();
}
