package com.codetrik.simpleConsumer.scheduler;

import com.codetrik.simpleConsumer.service.LoanService;
import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LoanScheduler {
    private final ExecutorService executorService;
    private final LoanService loanService;

    private Logger logger = LoggerFactory.getLogger("LoanScheduler");

    public LoanScheduler(@Qualifier("service-executor") ExecutorService executorService,
                         @Qualifier("loan-service") LoanService loanService) {
        this.executorService = executorService;
        this.loanService = loanService;
    }

    @Scheduled(fixedDelay = 3L, timeUnit = TimeUnit.SECONDS)
    public void consumeLoanApplicationMessage(){
        logger.info("[SCHEDULER INFO] consumeLoanMessage scheduler started");
        var box = new SimpleConsumerServiceBox(null,null);
        box.setExecutorService(this.executorService);
        box.asyncSubmitProcess(()->{
            this.loanService.consumeLoanApplicationProcess(box);
        });
    }
}
