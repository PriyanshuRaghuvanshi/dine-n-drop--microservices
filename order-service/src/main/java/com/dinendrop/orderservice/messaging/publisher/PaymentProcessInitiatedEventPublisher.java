package com.dinendrop.orderservice.messaging.publisher;

import com.dinendrop.orderservice.config.RabbitMQConfig;
import com.dinendrop.orderservice.messaging.events.PaymentProcessInitiatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentProcessInitiatedEventPublisher {

    private final RabbitTemplate rabbitTemplate;


    public void publishPaymentInitiated(PaymentProcessInitiatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_INITIATED_ROUTING_KEY,
                event
        );
        System.out.println("📤 Published PaymentInitiatedEvent: " + event);
    }
}
