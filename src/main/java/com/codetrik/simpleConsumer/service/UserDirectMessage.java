package com.codetrik.simpleConsumer.service;

import com.codetrik.Message;
import com.codetrik.dto.User;
import com.codetrik.event.MQEvent;
import com.codetrik.simpleConsumer.event.MQUserMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static com.codetrik.BeanQualifier.FASTER_XML_MAPPER;
import static com.codetrik.BeanQualifier.USER_DIRECT_MESSAGE;
import static com.codetrik.Constants.USER_EXCHANGE;
import static com.codetrik.Constants.USER_QUEUE_1;
import static com.codetrik.Constants.USER_QUEUE_1_KEY_1;

@Component
@Qualifier(USER_DIRECT_MESSAGE)
public class UserDirectMessage implements Message<User> {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper mapper;

    public UserDirectMessage(ApplicationEventPublisher applicationEventPublisher,
                             @Qualifier(FASTER_XML_MAPPER) ObjectMapper mapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.mapper = mapper;
    }

    @Override
    public void publishMessage(Channel channel, User user) throws Exception {

    }

    @Override
    public void consumeMessage(Channel channel) throws Exception {
        channel.exchangeDeclare(USER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(USER_QUEUE_1,false,false,false,null);
        channel.queueBind(USER_QUEUE_1,USER_EXCHANGE,USER_QUEUE_1_KEY_1);
        DeliverCallback deliverCallback = (consumerTag, message)->{
            var data = mapper.readValue(message.getBody(),User.class);
            applicationEventPublisher.publishEvent(new MQUserMessageEvent(this,new MQEvent(data)));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = (String consumerTag) -> {};
        channel.basicConsume(USER_QUEUE_1,false,deliverCallback,cancelCallback);
    }
}
