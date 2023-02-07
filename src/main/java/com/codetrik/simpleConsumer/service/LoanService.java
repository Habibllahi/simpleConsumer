package com.codetrik.simpleConsumer.service;

import com.codetrik.dto.LoanApplication;
import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

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


    public void consumeLoanApplicationProcess(){
        try {
            var recoverableChannel = this.connection.openChannel();
            if(recoverableChannel.isPresent()){
                this.loanMessage.consumeMessage(recoverableChannel.get());
            }else{
                logger.info("[CHANNEL] MQ channel creation failed ");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e);
        }
    }
}
