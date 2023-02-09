package com.codetrik.simpleConsumer.controller;

import com.codetrik.simpleConsumer.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

import static com.codetrik.BeanQualifier.SERVICE_EXECUTOR;
import static com.codetrik.BeanQualifier.USER_SERVICE;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    private final ExecutorService executorService;
    private final UserService userService;

    public Controller(@Qualifier(SERVICE_EXECUTOR) ExecutorService executorService, @Qualifier(USER_SERVICE) UserService userService) {
        this.executorService = executorService;
        this.userService = userService;
    }
}
