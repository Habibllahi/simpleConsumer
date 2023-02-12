package com.codetrik.simpleConsumer.scheduler;

import com.codetrik.simpleConsumer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.codetrik.BeanQualifier.ASYNC_SCHEDULER_EXECUTOR;
import static com.codetrik.BeanQualifier.USER_SERVICE;

@Component
public class UserScheduler {
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public UserScheduler(@Qualifier(USER_SERVICE) UserService userService) {
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 2L, timeUnit = TimeUnit.SECONDS)
    @Async(ASYNC_SCHEDULER_EXECUTOR)
    public void consumeUserMessage(){
        logger.info("[USER SCHEDULER INFO] consumeUserMessage scheduler started");
        this.userService.consumeUser();
    }
}
