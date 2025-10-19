package com.dinendrop.paymentservice.events;


import com.dinendrop.paymentservice.constants.PaymentStatus;
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
