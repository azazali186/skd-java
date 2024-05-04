package com.street.common.utils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConnection {

    public RabbitTemplate connectRabbitMq(String hostname, String username, String password, String virtualHost, Integer port) {

        hostname = (hostname != null) ? hostname : "127.0.0.1";
        username = (username != null) ? username : "admin";
        password = (password != null) ? password : "Aj189628@";
        virtualHost = (virtualHost != null) ? virtualHost : "/street-skd";
        port = (port != null) ? port : 5672;

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

    private MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

