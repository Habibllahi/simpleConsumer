package com.codetrik.simpleConsumer.service;

import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Qualifier("user-service")
public class UserService {
    private final UserMessage userMessage;
    private final Connection connection;

    private Logger logger = LoggerFactory.getLogger("UserService");

    public UserService(@Qualifier("user-message") UserMessage userMessage, @Qualifier("rabbit-mq-connection") Connection connection) {
        this.userMessage = userMessage;
        this.connection = connection;
    }

    public void consumeUser(SimpleConsumerServiceBox box){
        try {
            var recoverableChannel = this.connection.createChannel();
            box.setChannel(recoverableChannel);
            userMessage.consumeMessage(box.getChannel());
        } catch (IOException e) {
            box.getServiceResponse().setErrorMessage(e.getMessage());
            logger.error(e.getMessage(),e);
        }
    }
    }
