package com.codetrik.simpleConsumer.service;

import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.codetrik.BeanQualifier.LOAN_MESSAGE;
import static com.codetrik.BeanQualifier.LOAN_SERVICE;
import static com.codetrik.BeanQualifier.RABBIT_MQ_CONNECTION;

@Service
@Qualifier(LOAN_SERVICE)
public class LoanService {
    private final LoanMessage loanMessage;
    private final Connection connection;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public LoanService(@Qualifier(LOAN_MESSAGE) LoanMessage loanMessage, @Qualifier(RABBIT_MQ_CONNECTION) Connection connection) {
        this.loanMessage = loanMessage;
        this.connection = connection;
    }


    public void consumeLoanApplicationProcess(){
        try {
            var recoverableChannel = this.connection.openChannel();
            if(recoverableChannel.isPresent()){
                this.loanMessage.consumeMessage(recoverableChannel.get());
            }else{
                logger.info("[LOAN CHANNEL] MQ channel creation failed ");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e);
        }
    }
}
