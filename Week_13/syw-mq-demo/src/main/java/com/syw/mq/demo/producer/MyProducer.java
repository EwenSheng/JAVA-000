package com.syw.mq.demo.producer;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.Topic;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/9 20:37
 * @description:
 */
@Component
public class MyProducer {

    @Resource
    private Topic topic;

    /* JmsMessagingTemplate是发送消息的工具类，封装了JmsTemplate */
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessage(Destination destination, final String message) {
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

    public void sendTopic(String message) {
        jmsMessagingTemplate.convertAndSend(topic, message);
    }

}
