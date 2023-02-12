package com.codetrik.simpleConsumer.event.listner;

import com.codetrik.simpleConsumer.event.MQUserMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MQUserMessageEventListener implements ApplicationListener<MQUserMessageEvent> {
    private Logger logger = LoggerFactory.getLogger("MQUserMessageEventListener");
    @Override
    public void onApplicationEvent(MQUserMessageEvent event) {
        logger.info("[USER MESSAGE PROCESSING] user name is " + event.getUserMQMessage().getMessage().getFirstName());

    }
}
