package com.codetrik.simpleConsumer.controller;

import com.codetrik.Message;
import com.codetrik.request.UserServiceRequest;
import com.codetrik.response.UserServiceResponse;
import com.codetrik.simpleConsumer.service.UserService;
import com.codetrik.simpleConsumer.setup.SimpleConsumerServiceBox;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final ExecutorService executorService;
    private final UserService userService;

    public UserController(@Qualifier("service-executor") ExecutorService executorService,@Qualifier("user-service") UserService userService) {

        this.executorService = executorService;
        this.userService = userService;
    }

    @GetMapping("/user")
    public DeferredResult<ResponseEntity<?>> postUser(){
        var result = new DeferredResult<ResponseEntity<?>>();
        var box = new SimpleConsumerServiceBox(null, null);
        box.setExecutorService(this.executorService);
        box.asyncSubmitProcess(()->{
            //call service function
            this.userService.consumeUser(box);
            result.setResult(new ResponseEntity<>(HttpStatus.OK));
        });
        result.onCompletion(()-> box.doPostProcessing());
        return result;
    }
}
