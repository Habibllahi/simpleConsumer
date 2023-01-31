package com.codetrik.simpleConsumer.service;

import com.codetrik.Message;
import com.codetrik.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.codetrik.Constants.USER_QUEUE;

@Service
@Getter
@Setter
@Qualifier("user-message")
public class UserMessage implements Message<User> {
    private Logger logger = LoggerFactory.getLogger("UserMessage");
    @Override
    public void publishMessage(Channel channel, User user) {
    }

    @Override
    public void consumeMessage(Channel channel) {
        AtomicReference<User> atomicUser = new AtomicReference<>();
        try {
            var mapper = new ObjectMapper();
            channel.queueDeclare(USER_QUEUE,false,false,false,null);
            //this is asynchronous, provided callback will sure be executed in the future on another thread,
            // but message retrieved from broker is not immediately available
            channel.basicConsume(USER_QUEUE,true,(String consumerTag, Delivery delivery)-> {
                var message = delivery.getBody();
                logger.info("[X] message received" + message);
                atomicUser.set(mapper.readValue(message, User.class));
                logger.info("[X] user firstname : " + atomicUser.get().getFirstName());
            },(String consumerTag)->{
                logger.info("[y] message with tag "+consumerTag+" not found");
            });

        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }
}
