package com.codetrik.simpleConsumer.scheduler;

import com.codetrik.response.UserServiceResponse;
import com.codetrik.simpleConsumer.service.LoanService;
import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
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
    @Async("async-executor")
    public void consumeLoanApplicationMessage(){
        logger.info("[SCHEDULER INFO] consumeLoanMessage scheduler started");
        var box = new SimpleConsumerServiceBox(null,new UserServiceResponse());
        box.setExecutorService(this.executorService);
        this.loanService.consumeLoanApplicationProcess(box);
        var d = Optional.ofNullable(box.getServiceResponse().getLoanApplication());
        logger.info("[ACKNOWLEDGE] loan applicant is "+(d.isPresent()? d.get().getName() : ""));
        box.doPostProcessing();
    }
}
