package com.dinendrop.paymentservice.service;


import com.dinendrop.paymentservice.constants.PaymentStatus;
import com.dinendrop.paymentservice.events.PaymentProcessCompletedEvent;
import com.dinendrop.paymentservice.events.PaymentProcessInitiatedEvent;
import com.dinendrop.paymentservice.publisher.PaymentProcessCompletedEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProbabilityService {

    private final PaymentProcessCompletedEventPublisher paymentProcessCompletedEventPublisher ;

    @Value("${payment.success-rate}")
    private double successRate;


    public void completePayment(PaymentProcessInitiatedEvent event) {

        boolean isSuccess = ThreadLocalRandom.current().nextDouble() < successRate ;

        PaymentProcessCompletedEvent completedEvent = new PaymentProcessCompletedEvent(
                event.getOrderId(),
                isSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED
        );
        System.out.println(" PaymentCompletedEvent: " + completedEvent);

        paymentProcessCompletedEventPublisher.publishPaymentProcessCompletedEvent(completedEvent);

    }
}
