package com.codetrik.simpleConsumer.service;

import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.codetrik.BeanQualifier.CAR_INSURANCE_SERVICE;

@Service
@Qualifier(CAR_INSURANCE_SERVICE)
public class CarInsuranceService {
    private final Connection connection;
    private final CarInsuranceMessage carInsuranceMessage;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public CarInsuranceService(Connection connection, CarInsuranceMessage carInsuranceMessage) {
        this.connection = connection;
        this.carInsuranceMessage = carInsuranceMessage;
    }

    public void consumeCarInsuranceMessage(){
        try {
            var recoverableChannel = this.connection.openChannel();
            if(recoverableChannel.isPresent()){
                this.carInsuranceMessage.consumeMessage(recoverableChannel.get());
            }else{
                logger.info("[CHANNEL] MQ channel creation failed ");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e);
        }
    }
}
