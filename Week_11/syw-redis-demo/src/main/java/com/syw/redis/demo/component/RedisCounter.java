package com.syw.redis.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/7 20:06
 * @description: redis 计数器
 */
@Component
public class RedisCounter {

    private Long count = 0L;

    private final static String KEY = "inventory-counter";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setCount(Long count) {
        this.count = count;
        redisTemplate.delete(KEY);
    }

    public boolean reduce() {

        Long leftCount = redisTemplate.opsForValue().increment(KEY, 1);

        if (Objects.isNull(leftCount)) {
            return false;
        }

        return leftCount <= count;
    }
}
