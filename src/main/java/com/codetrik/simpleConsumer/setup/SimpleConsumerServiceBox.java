package com.codetrik.simpleConsumer.setup;

import com.codetrik.common.AbstractServiceBox;
import com.codetrik.request.UserServiceRequest;
import com.codetrik.response.UserServiceResponse;


public class SimpleConsumerServiceBox extends
        AbstractServiceBox<UserServiceRequest, UserServiceResponse> {


    public SimpleConsumerServiceBox(UserServiceRequest serviceRequest, UserServiceResponse serviceResponse) {
        super(serviceRequest, serviceResponse);
    }
}
