package com.syw.kafka.consumer.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/12 19:52
 * @description:
 */
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"topic-idea-demo"})
    public void consumer(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println("----------------- record =" + record);
            System.out.println("------------------ message =" + message);
        }
    }
}
