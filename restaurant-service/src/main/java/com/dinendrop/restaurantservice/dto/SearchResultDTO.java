package com.dinendrop.restaurantservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDTO {

    // "MENU_ITEM" or "RESTAURANT"
    private String type;

    // Display name (Menu item name or Restaurant name)
    private String name;

    // For menu items, helpful to show which restaurant it belongs to
    private String restaurantName;

    // IDs to let client navigate
    private String id;             // itemId for MENU_ITEM, restaurantId for RESTAURANT
    private String restaurantId;   // only set for MENU_ITEM
}
