package com.dinendrop.orderservice.messaging.events;

import com.dinendrop.orderservice.constants.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessCompletedEvent {
    private String orderId;
    private PaymentStatus paymentStatus;
}
