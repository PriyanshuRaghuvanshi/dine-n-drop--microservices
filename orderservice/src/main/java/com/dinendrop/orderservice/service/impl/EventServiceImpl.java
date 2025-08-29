package com.dinendrop.orderservice.service.impl;

import com.dinendrop.orderservice.constants.PaymentStatus;
import com.dinendrop.orderservice.messaging.events.OrderStatusChangedEvent;
import com.dinendrop.orderservice.messaging.events.PaymentProcessInitiatedEvent;
import com.dinendrop.orderservice.messaging.publisher.OrderStatusChangedEventPublisher;
import com.dinendrop.orderservice.messaging.publisher.PaymentProcessInitiatedEventPublisher;
import com.dinendrop.orderservice.model.Order;
import com.dinendrop.orderservice.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final OrderStatusChangedEventPublisher orderStatusPublisher;
    private final PaymentProcessInitiatedEventPublisher paymentPublisher;

    @Override
    @Async("taskExecutor")
    public void publishOrderStatusChangedEvent(Order order) {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                order.getId(),
                order.getUserId(),
                order.getRestaurantId(),
                order.getStatus(),
                LocalDateTime.now()
        );
        orderStatusPublisher.publishOrderStatusChanged(event);
    }

    @Override
    @Async("taskExecutor")
    public void publishPaymentInitiatedEvent(Order order) {


        PaymentProcessInitiatedEvent event = new PaymentProcessInitiatedEvent(
                order.getId(),
                order.getUserId(),
                order.getOrderTotal(),
                order.getPaymentMethod()
        );

        paymentPublisher.publishPaymentInitiated(event);
    }
}
