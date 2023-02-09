package com.codetrik.simpleConsumer.service;

import com.codetrik.Message;
import com.codetrik.dto.LoanApplication;
import com.codetrik.dto.Response;
import com.codetrik.event.MQEvent;
import com.codetrik.simpleConsumer.event.MQLoanMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.codetrik.BeanQualifier.LOAN_MESSAGE;
import static com.codetrik.Constants.LOAN_QUEUE;

@Service
@Getter
@Setter
@Qualifier(LOAN_MESSAGE)
public class LoanMessage implements Message<LoanApplication> {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void publishMessage(Channel channel, LoanApplication loanApplication) {
    }

    @Override
    public void consumeMessage(Channel channel) throws Exception {
        var consumeFromQueue = channel.queueDeclare(LOAN_QUEUE,false,
                false,false,null); //declare a server-named Queue and get the Queue name
        //this call is sync, will be executed surely sometimes in the future, however blocking queue ensure it blocks
        //until
        DeliverCallback deliverCallback =  (consumerTag, message)->{
            var data = mapper.readValue(message.getBody(),LoanApplication.class);
            var replyToQue = message.getProperties().getReplyTo();
            var propBuilder = new AMQP.BasicProperties().builder();
            var prop = propBuilder.correlationId(message.getProperties().getCorrelationId()).build();
            if(data != null){
                data.setResponse(new Response(Boolean.TRUE));
                //Provide feedback
                channel.basicPublish("",replyToQue,prop,mapper.writeValueAsBytes(data));
                applicationEventPublisher.publishEvent(new MQLoanMessageEvent(this,new MQEvent<>(data)));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };

        CancelCallback cancelCallback = (String consumerTag)->{};

        channel.basicConsume(LOAN_QUEUE,false,deliverCallback,cancelCallback);
    }


}
