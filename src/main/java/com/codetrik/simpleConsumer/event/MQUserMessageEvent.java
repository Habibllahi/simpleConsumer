package com.codetrik.simpleConsumer.event;

import com.codetrik.dto.User;
import com.codetrik.event.MQEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MQUserMessageEvent extends ApplicationEvent {
    MQEvent<User> userMQMessage;
    public MQUserMessageEvent(Object source, MQEvent<User> userMQMessage) {
        super(source);
        this.userMQMessage = userMQMessage;
    }
}
