package syw.mq.v2.server;

import syw.mq.v2.consumer.Consumer;
import syw.mq.v2.producer.Producer;
import syw.mq.v2.queue.MessageQueue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/19 15:29
 * @description: Broker+Connection
 */
public class Broker {

    public static final int CAPACITY = 10000;

    private final Map<String, MessageQueue> mqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name) {
        mqMap.putIfAbsent(name, new MessageQueue(name, CAPACITY));
    }

    public MessageQueue findKmq(String topic) {
        return this.mqMap.get(topic);
    }

    public Producer createProducer() {
        return new Producer(this);
    }

    public Consumer createConsumer() {
        return new Consumer(this);
    }
}
