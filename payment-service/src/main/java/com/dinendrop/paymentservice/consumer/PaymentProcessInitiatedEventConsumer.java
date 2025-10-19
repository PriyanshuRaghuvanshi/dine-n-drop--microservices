package com.dinendrop.paymentservice.consumer;

import com.dinendrop.paymentservice.config.RabbitMQConfig;
import com.dinendrop.paymentservice.events.PaymentProcessCompletedEvent;
import com.dinendrop.paymentservice.events.PaymentProcessInitiatedEvent;
import com.dinendrop.paymentservice.service.ProbabilityService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class PaymentProcessInitiatedEventConsumer {

    private final ProbabilityService probabilityService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_INITIATED_QUEUE)
    public void consumePaymentInitiated(PaymentProcessInitiatedEvent event) {
        System.out.println(" Received PaymentInitiatedEvent: " + event);

        probabilityService.completePayment(event);


    }
}
