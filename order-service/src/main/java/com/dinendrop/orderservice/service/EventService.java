package com.dinendrop.orderservice.service;

import com.dinendrop.orderservice.model.Order;

public interface EventService {

    void publishOrderStatusChangedEvent(Order order);

    void publishPaymentInitiatedEvent(Order order);
}
