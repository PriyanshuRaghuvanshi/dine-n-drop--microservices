package com.dinendrop.paymentservice.events;


import com.dinendrop.paymentservice.constants.PaymentMethod;
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
