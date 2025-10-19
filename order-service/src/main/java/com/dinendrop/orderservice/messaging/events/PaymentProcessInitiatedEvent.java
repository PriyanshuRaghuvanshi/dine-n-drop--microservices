package com.dinendrop.orderservice.messaging.events;

import com.dinendrop.orderservice.constants.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessInitiatedEvent {
    private String orderId;
    private String userId;
    private Double amount;
    private PaymentMethod paymentMethod;
}
