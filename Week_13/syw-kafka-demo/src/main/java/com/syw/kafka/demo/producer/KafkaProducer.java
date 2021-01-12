package com.syw.kafka.demo.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syw.kafka.demo.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/12 19:49
 * @description:
 */
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    public void send(String str) {

        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(str+UUID.randomUUID().toString());
        message.setSendTime(new Date());

        System.out.println("+++++++++++++++++++++  message = " + gson.toJson(message));

        kafkaTemplate.send("topic-idea-demo","syw", gson.toJson(message));
    }
}
