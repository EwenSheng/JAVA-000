package syw.mq;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
class SywMqApplicationTests {

    private final Lock lock = new ReentrantLock();

    @Test
    void contextLoads() {


    }

    @SneakyThrows
    public static void main(String[] args) {
        long time = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long deadline = time + 6000L;

        while (time <= deadline) {
            System.out.println("time:" + time + ",deadline:" + deadline);
            time = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            TimeUnit.MILLISECONDS.sleep(100L);
        }

        System.out.println("exit()");
    }

}
