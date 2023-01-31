package com.codetrik.simpleConsumer.service;

import com.codetrik.Constants;
import com.codetrik.Message;
import com.codetrik.dto.LoanApplication;
import com.codetrik.dto.LoanResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.codetrik.Constants.LOAN_QUEUE;
import static com.codetrik.Constants.LOAN_TEMP_QUEUE;

@Service
@Getter
@Setter
@Qualifier("loan-message")
public class LoanMessage implements Message<LoanApplication> {
    private Logger logger = LoggerFactory.getLogger("LoanMessage");
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void publishMessage(Channel channel, LoanApplication loanApplication) throws IOException {
    }

    @Override
    public LoanApplication consumeMessage(Channel channel) throws IOException {
        var consumeFromQueue = channel.queueDeclare(LOAN_QUEUE,false,
                false,false,null); //declare a server-named Queue and get the Queue name

        DeliverCallback deliverCallback =  (consumerTag, message)->{
            var data = mapper.readValue(message.getBody(),LoanApplication.class);
            var replyToQue = message.getProperties().getReplyTo();
            var propBuilder = new AMQP.BasicProperties().builder();
            var prop = propBuilder.correlationId(message.getProperties().getCorrelationId()).build();
            if(data != null){
                logger.info("[ACKNOWLEDGE] loan applicant is "+ data.getName());
                data.setResponse(new LoanResponse());
                data.getResponse().setOk(Boolean.TRUE);
                //Provide feedback
                channel.basicPublish("",replyToQue,prop,mapper.writeValueAsBytes(data));
            }
        };

        CancelCallback cancelCallback = (String consumerTag)->{};

        channel.basicConsume(LOAN_QUEUE,true,deliverCallback,cancelCallback);
        return null;
    }
}
