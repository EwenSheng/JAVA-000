/**
 * @author: Ewen-Sheng
 * @date: 2021/1/9 20:39
 * @description:
 */

import com.syw.mq.demo.SywMQDemoApplication;
import com.syw.mq.demo.producer.MyProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = SywMQDemoApplication.class)
public class QueueTest {

    @Resource
    private MyProducer producer;

    @Test
    public void testQueue() {
        producer.sendMessage(
                new ActiveMQQueue("syw.queue"),
                "hello,ActiveMQ (mode: queue)"
        );
    }

    @Test
    public void testTopic() {
        producer.sendTopic("hello,ActiveMQ (mode: topic)");
    }

    @Test
    public void testTogether() {

        producer.sendMessage(
                new ActiveMQQueue("syw.queue"),
                "hello,ActiveMQ testTogether (mode: queue)"
        );

        producer.sendTopic("hello,ActiveMQ testTogether (mode: topic)");
    }
}
