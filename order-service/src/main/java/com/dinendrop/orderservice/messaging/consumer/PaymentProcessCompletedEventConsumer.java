package com.dinendrop.orderservice.messaging.consumer;

import com.dinendrop.orderservice.config.RabbitMQConfig;
import com.dinendrop.orderservice.constants.OrderStatus;
import com.dinendrop.orderservice.constants.PaymentStatus;
import com.dinendrop.orderservice.messaging.events.PaymentProcessCompletedEvent;
import com.dinendrop.orderservice.model.Order;
import com.dinendrop.orderservice.repository.OrderRepository;
import com.dinendrop.orderservice.service.EventService;
import com.dinendrop.orderservice.utils.AfterCommitExecutor;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentProcessCompletedEventConsumer {

    private final EventService eventService;
    private final AfterCommitExecutor afterCommitExecutor;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    @Transactional
    public void consumePaymentCompleted(PaymentProcessCompletedEvent event) {
        System.out.println("📥 Received PaymentCompletedEvent: " + event);

        // Update order in DB
        Optional<Order> optionalOrder = orderRepository.findById(event.getOrderId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // update payment status
            if (event.getPaymentStatus()==PaymentStatus.SUCCESS) {
                order.setPaymentStatus(PaymentStatus.SUCCESS);
            } else {
                order.setPaymentStatus(PaymentStatus.FAILED);
                order.setStatus(OrderStatus.CANCELLED);
                afterCommitExecutor.execute(() -> eventService.publishOrderStatusChangedEvent(order));
            }

            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            System.out.println(" Updated order " + event.getOrderId() +
                    " with payment status " + order.getPaymentStatus());
        } else {
            System.err.println(" Order not found for ID: " + event.getOrderId());
        }
    }


}
