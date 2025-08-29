package com.dinendrop.orderservice.dto;

import com.dinendrop.orderservice.constants.OrderStatus;
import com.dinendrop.orderservice.constants.PaymentMethod;
import com.dinendrop.orderservice.constants.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponseDTO {
    private String id;
    private String userId;
    private String restaurantId;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponseDTO> items;
    private Double orderTotal;
    private PaymentStatus paymentStatus;
}
