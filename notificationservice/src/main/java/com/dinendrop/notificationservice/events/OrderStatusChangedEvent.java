package com.dinendrop.notificationservice.events;

import com.dinendrop.notificationservice.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangedEvent {
    private String orderId;
    private String userId;
    private String restaurantId;
    private OrderStatus status;
    private LocalDateTime timestamp;
}
