package com.codetrik.simpleConsumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrencyConfig {
    @Bean("rabbitmq-executor")
    public ExecutorService rabbitMqExecutor(){
        return Executors.newFixedThreadPool(10);
    }
    @Bean("service-executor")
    public ExecutorService serviceExecutor(){
        return Executors.newScheduledThreadPool(10);
    }
    @Bean("async-executor")
    public ExecutorService asyncExecutor(){
        return Executors.newScheduledThreadPool(2);
    }
}
