package com.syw.ss.xa.example.service;

import com.syw.ss.xa.example.dao.mapper.OrderMapper;
import com.syw.ss.xa.example.dao.model.Order;
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

    public Order get(Long id) {
        return mapper.get(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void insert() {

        System.out.println("========== OrderService.insert() ==========");

        StopWatch stopWatch = new StopWatch("insert order");
        startTheWatch(stopWatch);

        for (int index = 0; index < 1000; index++) {
            int userId = ThreadLocalRandom.current().nextInt(10);

            System.out.println("userId: " + userId + ",%2:" + (userId % 2));

            mapper.insert(userId, 0);
        }

        stopTheWatch(stopWatch);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void update(Long id, Integer status) {

        mapper.update(id, status);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void delete(Long id) {

        mapper.delete(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void xaOperating(Long ds0Id, Long ds1Id) {

        mapper.update(ds0Id, 1);

        mapper.update(ds1Id, 1);

        throw new RuntimeException("STW");
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
