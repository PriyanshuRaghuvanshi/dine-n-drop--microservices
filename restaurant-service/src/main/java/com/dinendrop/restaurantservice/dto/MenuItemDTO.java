package com.dinendrop.restaurantservice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MenuItemDTO {
    private String id;
    private String name;
    private Integer price;
    private String category;
    private Boolean available;
    private String restaurantId;
}
