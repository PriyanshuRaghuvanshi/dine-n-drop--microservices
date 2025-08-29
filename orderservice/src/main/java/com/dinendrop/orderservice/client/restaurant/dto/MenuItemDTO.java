package com.dinendrop.orderservice.client.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {
    private String id;
    private String name;
    private Integer price;
    private String category;
    private Boolean available;
    private String restaurantId;
}
