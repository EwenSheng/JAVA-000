package com.syw.ss.xa.example.service;

import com.syw.ss.xa.example.dao.mapper.OrderMapper;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 11:40
 * @description:
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper mapper;

    @Transactional(rollbackFor = RuntimeException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void insert() {

        System.out.println("========== OrderService.insert() ==========");

        StopWatch stopWatch = new StopWatch("insert order");
        startTheWatch(stopWatch);

        for (int index = 0; index < 1000; index++) {
            int userId = ThreadLocalRandom.current().nextInt(10);

            System.out.println("userId: " + userId + ",%2:" + (userId % 2));

            mapper.insert((long) userId, 0);
        }

        stopTheWatch(stopWatch);

        /*throw new RuntimeException("STW");*/
    }


    private void startTheWatch(StopWatch stopWatch) {
        stopWatch.start();
    }

    private void stopTheWatch(StopWatch stopWatch) {
        stopWatch.stop();
        System.out.println("========== OrderService.insert() ==========");
        System.out.println(stopWatch.prettyPrint());
    }
}
