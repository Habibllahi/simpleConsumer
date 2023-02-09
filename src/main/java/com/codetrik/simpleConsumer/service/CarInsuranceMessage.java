package com.codetrik.simpleConsumer.service;

import com.codetrik.Message;
import com.codetrik.dto.CarInsurance;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.codetrik.BeanQualifier.FASTER_XML_MAPPER;
import static com.codetrik.Constants.CORRELATION_ID_2;
import static com.codetrik.Constants.INSURANCE_EXCHANGE;
import static com.codetrik.Constants.INSURANCE_QUEUE;

@Service
@Getter
@Setter
public class CarInsuranceMessage implements Message<CarInsurance> {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper mapper;

    public CarInsuranceMessage(ApplicationEventPublisher applicationEventPublisher,
                               @Qualifier(FASTER_XML_MAPPER) ObjectMapper mapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.mapper = mapper;
    }

    @Override
    public void publishMessage(Channel channel, CarInsurance carInsurance) throws Exception {

    }

    @Override
    public void consumeMessage(Channel channel) throws Exception {
        channel.exchangeDeclare(INSURANCE_EXCHANGE, BuiltinExchangeType.FANOUT);
        channel.queueDeclare(INSURANCE_QUEUE,false,false,false,null);
        channel.queueBind(INSURANCE_QUEUE,INSURANCE_EXCHANGE,"");
        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            if(message.getProperties().getCorrelationId().equals(CORRELATION_ID_2)){

            }
        };
    }
}
