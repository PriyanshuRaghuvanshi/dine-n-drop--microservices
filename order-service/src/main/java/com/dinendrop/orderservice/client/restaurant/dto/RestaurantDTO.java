package com.dinendrop.orderservice.client.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private String id;
    private String name;
    private String areaCode;
    private String city;
    private LocalTime openAt;
    private LocalTime closeAt;
    private boolean online;
    private boolean diningEnabled;
}
