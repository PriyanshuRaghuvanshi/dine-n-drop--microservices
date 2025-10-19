package com.dinendrop.paymentservice.publisher;

import com.dinendrop.paymentservice.events.PaymentProcessCompletedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentProcessCompletedEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";

    public  void publishPaymentProcessCompletedEvent(PaymentProcessCompletedEvent event) {
        rabbitTemplate.convertAndSend(PAYMENT_EXCHANGE, PAYMENT_COMPLETED_ROUTING_KEY, event);
        System.out.println("Published PaymentCompletedEvent: " + event);
    }
}
