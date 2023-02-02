package com.codetrik.simpleConsumer.service;

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


    public void consumeLoanApplicationProcess(SimpleConsumerServiceBox box){
        try {
            int channelNumber = 105;
            var recoverableChannel = this.connection.openChannel();
            if(recoverableChannel.isPresent()){
                box.setChannel(recoverableChannel.get());
                var loanApplication = this.loanMessage.consumeMessage(box.getChannel());
                box.getServiceResponse().setLoanApplication(loanApplication);
            }else{
                logger.info("[CHANNEL] MQ channel creation failed ");
            }
        } catch (Exception e) {
            box.getServiceResponse().setErrorMessage(e.getMessage());
            this.logger.error(e.getMessage(),e);
        }
    }
}
