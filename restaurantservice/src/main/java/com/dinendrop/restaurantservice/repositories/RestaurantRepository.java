package com.dinendrop.restaurantservice.repositories;

import com.dinendrop.restaurantservice.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository  extends JpaRepository<Restaurant, String> {

}
