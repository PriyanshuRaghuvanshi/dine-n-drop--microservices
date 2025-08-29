package com.dinendrop.orderservice.repository;

import com.dinendrop.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserId(String userId);
    List<Order> findByRestaurantId(String restaurantId);
}
