package com.codetrik.simpleConsumer.scheduler;

import com.codetrik.simpleConsumer.service.CarInsuranceMessage;
import com.codetrik.simpleConsumer.service.CarInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.codetrik.BeanQualifier.ASYNC_SCHEDULER_EXECUTOR;
import static com.codetrik.BeanQualifier.CAR_INSURANCE_SERVICE;

@Component
public class InsuranceScheduler {
    private final CarInsuranceService insuranceService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public InsuranceScheduler(@Qualifier(CAR_INSURANCE_SERVICE) CarInsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Scheduled(fixedDelay = 2L, timeUnit = TimeUnit.SECONDS)
    @Async(ASYNC_SCHEDULER_EXECUTOR)
    public void processFeedBack(){
        logger.info("[SCHEDULER INFO] insurance message feedback scheduler started successfully");
        this.insuranceService.consumeCarInsuranceMessage();
    }
}
