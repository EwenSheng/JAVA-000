package syw.mq.v2.consumer;

import syw.mq.v2.enity.Message;
import syw.mq.v2.queue.MessageQueue;
import syw.mq.v2.server.Broker;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/19 14:26
 * @description:
 */
public class Consumer<T> {

    private final Broker broker;

    private MessageQueue mq;

    private final Map<Integer, Integer> map = new ConcurrentHashMap<>(10);

    public Consumer(Broker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {

        this.mq = this.broker.findKmq(topic);

        if (Objects.isNull(mq)) {
            throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        }
    }

    public Message<T> poll(Integer id) {

        AtomicInteger offset = new AtomicInteger(0);

        if (map.containsKey(id)) {
            offset = new AtomicInteger(map.get(id));
        } else {
            map.put(id, offset.get());
        }

        Message<T> message = mq.poll(offset.get());

        if (Objects.nonNull(message)) {
            map.put(id, offset.incrementAndGet());
        }

        return message;
    }
}
