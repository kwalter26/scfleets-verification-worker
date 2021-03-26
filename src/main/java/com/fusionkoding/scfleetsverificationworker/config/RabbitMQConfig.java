package com.fusionkoding.scfleetsverificationworker.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Priority;
import java.net.URI;
import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    @Getter
    private String verifyQueue;
    @Getter
    private String authQueue;
    private String verifyExchange;
    @Getter
    private String authExchange;
    private String routingKey;
    private String host;
    private String username;
    private String password;
    private String virtualHost;
    private boolean sslEnabled;

    public RabbitMQConfig(
            @Value("${spring.rabbitmq.queue.verify}") String verifyQueue,
            @Value("${spring.rabbitmq.queue.auth}") String authQueue,
            @Value("${spring.rabbitmq.exchange.verify}") String verifyExchange,
            @Value("${spring.rabbitmq.exchange.auth}") String authExchange,
            @Value("${spring.rabbitmq.routingkey}") String routingKey,
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password,
            @Value("${spring.rabbitmq.virtual-host}") String virtualHost,
            @Value("${spring.rabbitmq.ssl.enabled}") boolean sslEnabled
    ) {
        this.verifyQueue = verifyQueue;
        this.authQueue = authQueue + "-" + UUID.randomUUID();
        this.authExchange = authExchange;
        this.verifyExchange = verifyExchange;
        this.routingKey = routingKey;
        this.host = host;
        this.username = username;
        this.password = password;
        this.virtualHost = virtualHost;
        this.sslEnabled = sslEnabled;
    }

    @Bean
    Queue verifyQueue() {
        return new Queue(verifyQueue, true);
    }

    @Bean
    Queue authQueue() {
        return new Queue(authQueue, true, true, true);
    }

    @Bean
    Exchange verifyExchange() {
        return ExchangeBuilder.topicExchange(verifyExchange).durable(true).build();
    }

    @Bean
    Exchange authExchange() {
        return ExchangeBuilder.topicExchange(authExchange).durable(true).build();
    }

    @Bean
    Binding verifyBinding() {
        return BindingBuilder
                .bind(verifyQueue())
                .to(verifyExchange())
                .with(routingKey)
                .noargs();
    }

    @Bean
    Binding authBinding() {
        return BindingBuilder
                .bind(authQueue())
                .to(authExchange())
                .with("status")
                .noargs();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setUri(URI.create("amqps://" + host));
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        return cachingConnectionFactory;
    }

//    @Bean
//    public RabbitListenerContainerFactory rabbitListenerContainerFactory() {
//        return new RabbitListenerContainerFactory() {
//            @Override
//            public MessageListenerContainer createListenerContainer(RabbitListenerEndpoint endpoint) {
//                SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();
//
//                messageListenerContainer.
//
//                return  ;
//            }
//        };
//    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
