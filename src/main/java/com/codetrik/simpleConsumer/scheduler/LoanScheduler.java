package com.codetrik.simpleConsumer.scheduler;

import com.codetrik.simpleConsumer.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.codetrik.BeanQualifier.ASYNC_SCHEDULER_EXECUTOR;
import static com.codetrik.BeanQualifier.LOAN_SERVICE;

@Component
public class LoanScheduler {
    private final LoanService loanService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public LoanScheduler(
            @Qualifier(LOAN_SERVICE) LoanService loanService) {
        this.loanService = loanService;
    }

    @Scheduled(fixedDelay = 3L, timeUnit = TimeUnit.SECONDS)
    @Async(ASYNC_SCHEDULER_EXECUTOR)
    public void consumeLoanApplicationMessage(){
        logger.info("[SCHEDULER INFO] consumeLoanMessage scheduler started");
        this.loanService.consumeLoanApplicationProcess();
    }
}
