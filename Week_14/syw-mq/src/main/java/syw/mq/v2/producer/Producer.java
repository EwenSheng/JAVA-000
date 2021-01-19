package syw.mq.v2.producer;

import syw.mq.v2.enity.Message;
import syw.mq.v2.queue.MessageQueue;
import syw.mq.v2.server.Broker;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/19 15:16
 * @description:
 */
public class Producer<T> {

    private Broker broker;

    public Producer(Broker broker) {
        this.broker = broker;
    }

    public boolean send(String topic, Message<T> message) {

        MessageQueue kmq = this.broker.findKmq(topic);

        if (null == kmq) {
            throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        }

        return kmq.send(message);
    }
}
