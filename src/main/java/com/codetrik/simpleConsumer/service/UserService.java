package com.codetrik.simpleConsumer.service;

import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.codetrik.BeanQualifier.RABBIT_MQ_CONNECTION;
import static com.codetrik.BeanQualifier.USER_MESSAGE;
import static com.codetrik.BeanQualifier.USER_SERVICE;

@Service
@Qualifier(USER_SERVICE)
public class UserService {
    private final UserMessage userMessage;
    private final Connection connection;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public UserService(@Qualifier(USER_MESSAGE) UserMessage userMessage, @Qualifier(RABBIT_MQ_CONNECTION) Connection connection) {
        this.userMessage = userMessage;
        this.connection = connection;
    }

    public void consumeUser(){
        try {
            var recoverableChannel = this.connection.openChannel();
            if(recoverableChannel.isPresent()){
               userMessage.consumeMessage(recoverableChannel.get());
            }else{
                logger.info("[CHANNEL] MQ channel creation failed ");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    }

