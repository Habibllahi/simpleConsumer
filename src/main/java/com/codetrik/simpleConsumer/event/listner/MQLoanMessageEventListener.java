package com.codetrik.simpleConsumer.event.listner;

import com.codetrik.simpleConsumer.event.MQLoanMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MQLoanMessageEventListener implements ApplicationListener<MQLoanMessageEvent> {
    private Logger logger = LoggerFactory.getLogger("MQLoanMessageEventListener");
    @Override
    public void onApplicationEvent(MQLoanMessageEvent event) {
        logger.info("[LOAN MESSAGE PROCESSED] loan applicant name is " + event.getLoanMQMessage().getMessage().getName());
    }
}
