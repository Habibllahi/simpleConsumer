package com.codetrik.simpleConsumer.service;

import com.codetrik.Constants;
import com.codetrik.dto.LoanApplication;
import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.codetrik.Constants.LOAN_QUEUE;

@Service
@Qualifier("loan-service")
public class LoanService {
    private final LoanMessage loanMessage;
    private final Connection connection;

    private Logger logger = LoggerFactory.getLogger("LoanService");

    public LoanService(@Qualifier("loan-message") LoanMessage loanMessage, @Qualifier("rabbit-mq-connection") Connection connection) {
        this.loanMessage = loanMessage;
        this.connection = connection;
    }


    public void consumeLoanApplicationProcess(SimpleConsumerServiceBox box){
        try {
            var recoverableChannel = this.connection.createChannel(101);
            box.setChannel(recoverableChannel);
            this.loanMessage.consumeMessage(box.getChannel());
        } catch (IOException e) {
            box.getServiceResponse().setErrorMessage(e.getMessage());
            this.logger.error(e.getMessage(),e);
        }
    }
}
