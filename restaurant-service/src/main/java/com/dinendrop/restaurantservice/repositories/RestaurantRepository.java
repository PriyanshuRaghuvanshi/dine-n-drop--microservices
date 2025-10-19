package com.dinendrop.restaurantservice.repositories;

import com.dinendrop.restaurantservice.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RestaurantRepository  extends JpaRepository<Restaurant, String> {

}
