package com.syw.redis.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/7 19:32
 * @description: redis 锁
 */
@Component
public class RedisLock {

    private static final long DEFUALT_TIME = 60000;

    private static final long TRY_LOCK_TIME_OUT = 5000;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String UNLOCK_LUA_SCRIPTS =
            "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del',KEYS[1]) else return 0 end";

    /**
     * @author: Ewen
     * @date: 2021/1/7 19:47
     * @param: [key, value, millis]
     * @return: java.lang.Boolean
     * @description: 加锁
     */
    public Boolean tryLock(String key, String value, Long millis) {

        long start = System.currentTimeMillis();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration timeout = Objects.nonNull(millis) ? Duration.ofMillis(millis) : Duration.ofMillis(DEFUALT_TIME);

        while (true) {
            if (Boolean.TRUE.equals(operations.setIfAbsent(key, value, timeout))) {
                return true;
            }
            long l = System.currentTimeMillis() - start;
            if (l >= TRY_LOCK_TIME_OUT) {
                return false;
            }
        }
    }


    /**
     * @author: Ewen
     * @date: 2021/1/7 19:52
     * @param: [key, value]
     * @return: void
     * @description: 解锁
     */
    public Boolean unlock(String key, String value) {

        DefaultRedisScript<Long> lockScript = new DefaultRedisScript<>(UNLOCK_LUA_SCRIPTS, Long.class);

        return Long.valueOf(1).equals(stringRedisTemplate.execute(lockScript, Collections.singletonList(key), value));
    }
}
