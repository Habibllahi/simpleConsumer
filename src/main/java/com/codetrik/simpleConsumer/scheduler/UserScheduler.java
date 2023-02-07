package com.codetrik.simpleConsumer.scheduler;

import com.codetrik.simpleConsumer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class UserScheduler {
    private final ExecutorService executorService;
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger("UserScheduler");

    public UserScheduler(@Qualifier("service-executor") ExecutorService executorService,
                         @Qualifier("user-service") UserService userService) {
        this.executorService = executorService;
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 2L, timeUnit = TimeUnit.SECONDS)
    @Async("async-executor")
    public void consumeUserMessage(){
        logger.info("[SCHEDULER INFO] consumeUserMessage scheduler started");
        this.userService.consumeUser();
    }
}
