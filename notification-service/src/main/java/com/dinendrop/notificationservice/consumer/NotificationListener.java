package com.dinendrop.notificationservice.consumer;

import com.dinendrop.notificationservice.events.OrderStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationListener {
    @RabbitListener(queues = "order.notifications.queue")
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("🔔 Notification Event Received: Order {} for User {} is now {} at {}",
                event.getOrderId(),
                event.getUserId(),
                event.getStatus(),
                event.getTimestamp());
    }
}
