package com.dinendrop.orderservice.messaging.events;

import com.dinendrop.orderservice.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedEvent {
    private String orderId;
    private String userId;
    private String restaurantId;
    private OrderStatus status;
    private LocalDateTime timestamp;
}
