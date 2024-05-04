package com.street.core.listner;

import com.street.common.utils.RabbitMqConnection;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class RegisterListners {

    public void RegisterListeners() {
        RabbitMqConnection rabbitMqConnection = new RabbitMqConnection();
    }

    @RabbitListener(queues = "skd-register")
    public void handleRegister(String message) {
        System.out.println("Received message from queue " + "skd-register" + ": " + message);
    }


    @RabbitListener(queues = "skd-login")
    public void handleLogin(String message) {
        System.out.println("Received message from queue " + "skd-login" + ": " + message);
    }
}