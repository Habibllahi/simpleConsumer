package com.codetrik.simpleConsumer.service;

import com.codetrik.Message;
import com.codetrik.dto.User;
import com.codetrik.event.MQEvent;
import com.codetrik.simpleConsumer.event.MQUserMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.codetrik.Constants.USER_QUEUE;

@Service
@Getter
@Setter
@Qualifier("user-message")
public class UserMessage implements Message<User> {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    private Logger logger = LoggerFactory.getLogger("UserMessage");
    @Override
    public void publishMessage(Channel channel, User user) {
    }

    @Override
    public void consumeMessage(Channel channel) throws Exception {
        var mapper = new ObjectMapper();
        channel.queueDeclare(USER_QUEUE,false,false,false,null);
        //this is asynchronous, provided callback will sure be executed in the future on another thread,
        // but message retrieved from broker is not immediately available
        channel.basicConsume(USER_QUEUE,false,(String consumerTag, Delivery delivery)-> {
            var message = delivery.getBody();
            logger.info("[X] message received " + message);
            var data = mapper.readValue(message, User.class);
            applicationEventPublisher.publishEvent(new MQUserMessageEvent(this,new MQEvent<>(data)));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            },(String consumerTag)->{
                logger.info("[y] message with tag "+consumerTag+" not found");
            });
    }
}
