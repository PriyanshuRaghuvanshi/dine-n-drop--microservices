package com.dinendrop.paymentservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_INITIATED_QUEUE = "payment.initiated.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";
    public static final String PAYMENT_INITIATED_ROUTING_KEY = "payment.initiated";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.completed";

    // ===== Exchanges =====

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    // ===== Queues =====

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
