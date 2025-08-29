package com.dinendrop.orderservice.client.restaurant;

import com.dinendrop.orderservice.client.restaurant.dto.MenuItemDTO;
import com.dinendrop.orderservice.client.restaurant.dto.RestaurantDTO;
import com.dinendrop.orderservice.utils.TokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantClient {

    private final WebClient.Builder webClientBuilder;
    private final TokenExtractor tokenExtractor;

    // Base URL goes through service discovery or API gateway.
    private static final String BASE = "lb://api-gateway/restaurants";




    public RestaurantDTO getRestaurant(String restaurantId) {
        String token = tokenExtractor.getBearerToken();

        return webClientBuilder.build()
                .get()
                .uri(BASE + "/{id}", restaurantId)
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(RestaurantDTO.class)
                .block();
    }

    public List<MenuItemDTO> getRestaurantMenu(String restaurantId) {
        String token = tokenExtractor.getBearerToken();

        MenuItemDTO[] items = webClientBuilder.build()
                .get()
                .uri(BASE + "/{id}/menu", restaurantId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(MenuItemDTO[].class)
                .block();

        return items == null ? List.of() : Arrays.asList(items);
    }

    public MenuItemDTO getMenuItem(String restaurantId, String itemId) {
        String token = tokenExtractor.getBearerToken();
        return webClientBuilder.build()
                .get()
                .uri(BASE + "/{rid}/menu/{iid}", restaurantId, itemId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(MenuItemDTO.class)
                .block();
    }

}
