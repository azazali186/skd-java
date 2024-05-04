package com.street.common.utils;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    private final AmqpTemplate amqpTemplate;


    @Autowired
    public MessagePublisher() {
        RabbitMqConnection rabbitMqConnection = new RabbitMqConnection();
        this.amqpTemplate = rabbitMqConnection.connectRabbitMq(null, null, null, null, null);
    }

    public void publishMessage(String exchangeName, String routingKey, String message) {
        amqpTemplate.convertAndSend(exchangeName, routingKey, message);
        System.out.println("Message published: " + message);
    }
}
