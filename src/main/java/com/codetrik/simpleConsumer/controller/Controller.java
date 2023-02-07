package com.codetrik.simpleConsumer.controller;

import com.codetrik.simpleConsumer.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    private final ExecutorService executorService;
    private final UserService userService;

    public Controller(@Qualifier("service-executor") ExecutorService executorService, @Qualifier("user-service") UserService userService) {
        this.executorService = executorService;
        this.userService = userService;
    }
}
