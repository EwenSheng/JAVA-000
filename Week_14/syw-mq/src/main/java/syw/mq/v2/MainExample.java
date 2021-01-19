package syw.mq.v2;

import lombok.SneakyThrows;
import syw.mq.v2.consumer.Consumer;
import syw.mq.v2.enity.Message;
import syw.mq.v2.enity.Order;
import syw.mq.v2.factory.NamedThreadFactory;
import syw.mq.v2.producer.Producer;
import syw.mq.v2.server.Broker;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/19 16:02
 * @description:
 */
public class MainExample {

    private static final String TOPIC = "example";

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            2,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new NamedThreadFactory("v2-example")
    );

    @SneakyThrows
    public static void main(String[] args) {

        final boolean[] flag = new boolean[1];
        flag[0] = true;

        Broker broker = new Broker();
        broker.createTopic(TOPIC);

        consumerClient(flag, broker);

        producerClient(flag, broker);
    }

    private static void producerClient(boolean[] flag, Broker broker) throws InterruptedException, IOException {
        Producer<Order> producer = broker.createProducer();

        for (int index = 0; index < 10; index++) {
            producer.send(
                    TOPIC,
                    new Message<>(
                            null,
                            new Order((long) index, System.currentTimeMillis(), "CNY", 10d)
                    )
            );
            Thread.sleep(100);
        }

        Thread.sleep(500);

        System.out.println("Click any key to send a message; Click 'Q' or 'E' to exit the program...");

        while (true) {
            char c = (char) System.in.read();
            if (c > 20) {
                System.out.println(c);
                producer.send(
                        TOPIC,
                        new Message<>(
                                null,
                                new Order(10000L + c, System.currentTimeMillis(), "CNY", 10d)
                        )
                );
            }
            if (c == 'q' || c == 'e') {
                break;
            }
        }

        flag[0] = false;
    }

    private static void consumerClient(boolean[] flag, Broker broker) {
        Consumer<Order> consumer = broker.createConsumer();
        consumer.subscribe(TOPIC);
        executor.execute(() -> {
            while (flag[0]) {
                Message<Order> message = consumer.poll(1);
                if (Objects.nonNull(message)) {
                    System.out.println("id:1; ThreadName:" + Thread.currentThread().getName() + message.getBody());
                }
            }
            System.out.println("id:1; ThreadName:" + Thread.currentThread().getName() + " Consumer Program exit...");
        });

        executor.execute(() -> {
            while (flag[0]) {
                Message<Order> message = consumer.poll(2);
                if (Objects.nonNull(message)) {
                    System.out.println("id:2; ThreadName:" + Thread.currentThread().getName() + message.getBody());
                }
            }
            System.out.println("id:2; ThreadName:" + Thread.currentThread().getName() + " Consumer Program exit...");
        });


        executor.shutdown();
        System.out.println("All Consumer Program exit...");
    }
}
