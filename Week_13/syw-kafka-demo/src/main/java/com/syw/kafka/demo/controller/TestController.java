package com.syw.kafka.demo.controller;

import com.syw.kafka.demo.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/12 19:55
 * @description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KafkaProducer producer;

    @PostMapping("/send")
    public void send() {
        producer.send("syw");
    }
}
