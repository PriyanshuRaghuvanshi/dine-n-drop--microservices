package com.dinendrop.orderservice.messaging.publisher;

import com.dinendrop.orderservice.messaging.events.OrderStatusChangedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderStatusChangedEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    public static final String EXCHANGE = "order.exchange";
    public static final String ROUTING_KEY = "order.status.changed";

    public void publishOrderStatusChanged(OrderStatusChangedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
        System.out.println("Published event: " + event);
    }
}
