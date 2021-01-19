package syw.mq.v2.queue;

import syw.mq.v2.enity.Message;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/19 14:27
 * @description:
 */
public class MessageQueue {

    private String topic;

    private int capacity;

    private ArrayQueue<Message> queue;

    public MessageQueue(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new ArrayQueue<>(capacity);
    }

    public boolean send(Message message) {
        return queue.offer(message);
    }

    public Message poll(int front) {
        return queue.poll(front);
    }
}
