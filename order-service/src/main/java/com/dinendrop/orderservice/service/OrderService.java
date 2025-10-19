package com.dinendrop.orderservice.service;

import com.dinendrop.orderservice.constants.OrderStatus;
import com.dinendrop.orderservice.dto.OrderRequestDTO;
import com.dinendrop.orderservice.dto.OrderResponseDTO;
import com.dinendrop.orderservice.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO req);

    Optional<Order> getByIdEntity(String id);

    OrderResponseDTO getById(String id);

    List<OrderResponseDTO> getByUser(String userId);

    List<OrderResponseDTO> getByRestaurant(String restaurantId);

    OrderResponseDTO updateStatus(String orderId, OrderStatus status);

    boolean cancelOrder(String orderId);

}
