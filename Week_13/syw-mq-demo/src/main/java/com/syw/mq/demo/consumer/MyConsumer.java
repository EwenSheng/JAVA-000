package com.syw.mq.demo.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/9 20:37
 * @description:
 */
@Component
public class MyConsumer {

    @JmsListener(destination = "syw.queue", containerFactory = "queueListenerFactory")
    public void receiveQueue(String text) {
        System.out.println("receive queue , text: " + text);
    }

    @JmsListener(destination = "syw.topic", containerFactory = "topicListenerFactory")
    public void receiveTopic(String text) {
        System.out.println("receive topic , text: " + text);
    }
}
