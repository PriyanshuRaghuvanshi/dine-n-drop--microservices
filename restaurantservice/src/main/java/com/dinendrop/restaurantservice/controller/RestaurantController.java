package com.dinendrop.restaurantservice.controller;

import com.dinendrop.restaurantservice.dto.MenuItemDTO;
import com.dinendrop.restaurantservice.models.MenuItem;
import com.dinendrop.restaurantservice.models.Restaurant;
import com.dinendrop.restaurantservice.repositories.MenuItemRepository;
import com.dinendrop.restaurantservice.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepo;
    private final MenuItemRepository menuRepo;


    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable String id) {
        return restaurantRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Restaurant create(@RequestBody Restaurant restaurant) {
        restaurant.setId(UUID.randomUUID().toString());
        return restaurantRepo.save(restaurant);
    }


    @PostMapping("/{restaurantId}/menu")
    @Transactional
    public ResponseEntity<List<MenuItemDTO>> addMenuItem(@PathVariable String restaurantId, @RequestBody List<MenuItem> menuItems) {
        Optional<Restaurant> restaurantOpt = restaurantRepo.findById(restaurantId);

        if (restaurantOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Restaurant restaurant = restaurantOpt.get();
        List<MenuItemDTO> dtos = new ArrayList<>();

        for (MenuItem menuItem : menuItems) {
            menuItem.setId(UUID.randomUUID().toString());
            menuItem.setRestaurant(restaurant);

            MenuItem saved = menuRepo.save(menuItem);

            MenuItemDTO dto = new MenuItemDTO(
                    saved.getId(),
                    saved.getName(),
                    saved.getPrice(),
                    saved.getCategory(),
                    saved.getAvailable(),
                    restaurant.getId()
            );
            dtos.add(dto);
        }
            return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu")
    public List<MenuItem> getMenu(@PathVariable String id) {
        return restaurantRepo.findById(id)
                .map(Restaurant::getMenu)
                .orElse(Collections.emptyList());
    }

}
