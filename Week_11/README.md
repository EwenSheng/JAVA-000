## 题目

### 第一节课

1. （选做）命令行下练习操作 Redis 的各种基本数据结构和命令。
2. （选做）分别基于 jedis，RedisTemplate，Lettuce，Redission 实现 redis 基本操作的 demo，可以使用 spring-boot 集成上述工具。
3. （选做）spring 集成练习:

- 实现 update 方法，配合 @CachePut
- 实现 delete 方法，配合 @CacheEvict
- 将示例中的 spring 集成 Lettuce 改成 jedis 或 redisson

4. （必做）基于 Redis 封装分布式数据操作：

- 在 Java 中实现一个简单的分布式锁；
- 在 Java 中实现一个分布式计数器，模拟减库存。 5.（必做）基于 Redis 的 PubSub 实现订单异步处理

### 第二节课

1. （挑战☆）基于其他各类场景，设计并在示例代码中实现简单 demo：

- 实现分数排名或者排行榜；
- 实现全局 ID 生成；
- 基于 Bitmap 实现 id 去重；
- 基于 HLL 实现点击量计数； 以 redis 作为数据库，模拟使用 lua 脚本实现前面课程的外汇交易事务。

2. （挑战☆☆）升级改造项目：

- 实现 guava cache 的 spring cache 适配；
- 替换 jackson 序列化为 fastjson 或者 fst，kryo；
- 对项目进行分析和性能调优。

3. （挑战☆☆☆）以 redis 作为基础实现上个模块的自定义 rpc 的注册中心。

## 开发环境

```
个人终端：
处理器：AMD Ryzen 9 4900HS Radeon Graphics 3.00GHZ
RAM：40.0 GB(可用 39.4GB)
类型：x64处理器
```

```
JDK版本：
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
```

```
涉及工具：
IntelliJ IDEA 2020.2
Windows Terminal 1.3.2651.9
SuperBenchmarker 4.5.1
gateway-server-0.0.1-SNAPSHOT.jar -- 测试服务 端口8088
Google Chrome
Docker version 19.03.13
```

## 解题

### 在 Java 中实现一个简单的分布式锁

1. 加锁时,考虑并发导致加锁失败,采用 while (true)方式
2. 为了防止死锁,添加了加锁超时的机制
3. 释放锁时,为了保证操作原子性(先读后释放),采用Lua脚本方式

代码
~~~
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
~~~

执行结果
~~~
127.0.0.1:6379> get syw
"1"
-- 60秒自动过期
127.0.0.1:6379> get syw
(nil)
-- 手动释放锁
~~~

### 在 Java 中实现一个分布式计数器，模拟减库存。

代码
~~~
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
~~~

执行结果
~~~
Thread[pool-1-thread-1,5,main], 购买->成功
Thread[pool-1-thread-7,5,main], 购买->成功
Thread[pool-1-thread-2,5,main], 购买->成功
Thread[pool-1-thread-6,5,main], 购买->成功
Thread[pool-1-thread-8,5,main], 购买->成功
Thread[pool-1-thread-5,5,main], 购买->失败
Thread[pool-1-thread-3,5,main], 购买->失败
Thread[pool-1-thread-10,5,main], 购买->失败
Thread[pool-1-thread-4,5,main], 购买->失败
Thread[pool-1-thread-9,5,main], 购买->失败
~~~