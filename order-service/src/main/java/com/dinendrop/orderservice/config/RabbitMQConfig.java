package com.dinendrop.orderservice.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // ===== Order Notifications =====
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_NOTIFICATION_QUEUE = "order.notifications.queue";
    public static final String ORDER_STATUS_ROUTING_KEY = "order.status.changed";


    // ===== Payments =====
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_INITIATED_QUEUE = "payment.initiated.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";
    public static final String PAYMENT_INITIATED_ROUTING_KEY = "payment.initiated";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";

    // ===== Exchanges =====
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    // ===== Queues =====
    @Bean
    public Queue orderNotificationQueue() {
        return new Queue(ORDER_NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Queue paymentInitiatedQueue() {
        return new Queue(PAYMENT_INITIATED_QUEUE, true);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE, true);
    }

    // ===== Bindings =====
    @Bean
    public Binding orderNotificationBinding(Queue orderNotificationQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderNotificationQueue).to(orderExchange).with(ORDER_STATUS_ROUTING_KEY);
    }

    @Bean
    public Binding paymentInitiatedBinding(Queue paymentInitiatedQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentInitiatedQueue).to(paymentExchange).with(PAYMENT_INITIATED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentCompletedBinding(Queue paymentCompletedQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentCompletedQueue).to(paymentExchange).with(PAYMENT_COMPLETED_ROUTING_KEY);
    }

    // ===== JSON Converter =====
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
