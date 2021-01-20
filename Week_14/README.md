## 题目

### 第一节课

1. （选做）自己安装和操作RabbitMQ，RocketMQ，Pulsar，以及Camel和SI。
2. （必做）思考和设计自定义MQ第二个版本或第三个版本，写代码实现其中至少一个功能点，把设计思路和实现代码，提交到github。
    * version 1.0 (内存Queue) 基于内存Queue实现生产和消费API（已经完成）
        - 创建内存BlockingQueue，作为底层消息存储
        - 定义Topic，支持多个Topic
        - 定义Producer，支持Send消息
        - 定义Consumer，支持Poll消息
    * version 2.0 (自定义Queue) 去掉内存Queue，设计自定义Queue，实现消息确认和消费offset
        - 自定义内存Message数组模拟Queue
        - 使用指针记录当前消息写入位置
        - 对于每个命名消费者，用指针记录消费位置
    * version 3.0 (基于SpringMVC实现MQServer) 拆分broker和client(包括producer和consumer)
        - 将Queue保存到webserver端
        - 设计消息读写API接口，确认接口，提交offset接口
        - producer和consumer通过httpclient访问Queue
        - 实现消息确认，offset提交
        - 实现consumer从offset增量拉取
    * version 4.0 (功能完善MQ) 增加多种策略（各条之间没有关系，可以任意选择实现）
        - 考虑实现消息过期，消息重试，消息定时投递等策略
        - 考虑批量操作，包括读写，可以打包和压缩
        - 考虑消息清理策略，包括定时清理，按容量清理、LRU等
        - 考虑消息持久化，存入数据库，或WAL日志文件，或BookKeeper
        - 考虑将springmvc替换成netty下的tcp传输协议，rsocket/websocket
    * version 5.0 (体系完善MQ) 对接各种技术（各条之间没有关系，可以任意选择实现）
        - 考虑封装JMS1.1接口规范
        - 考虑实现STOMP消息规范
        - 考虑实现消息事务机制与事务管理器
        - 对接Spring
        - 对接Camel或SpringIntegration
        - 优化内存和磁盘的使用
3. （挑战☆☆☆☆☆）完成其他版本的要求。

### 第二节课

1. （选做）思考一下自己负责的系统，或者做过的系统，能否描述清楚其架构。
2. （选做）考虑一下，如果让你做一个针对双十一，某东某宝半价抢100个IPhone的 活动系统，你该如何考虑，从什么地方入手。
3. （选做）可以自行学习以下几本参考书。

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

### version 2.0 -- 已完成

#### 设计思路:

1. 先替换队列,把BlockingQueue换成数组;
2. 思考producer入队,在数组通过一个变量rear记录消息写入位置,并且定义了offer(T t)入队方法
3. 思考consumer,如何让多个consumer同时消费队列里的数据,并记录消费的位置(K,V 结构适合存储),参考Kafka设计并不是MQ本身记录的, 而是消费者自己记录自己要消费哪段的
   - 修改Queue只保留出队的行为,把记录位置交付给consumer本身去记录
   - 修改Consumer,通过ConcurrentHashMap+AtomicInteger记录每个Consumer-Id对应的消费位置
   - 简单实现了一个确认机制, 先获取上一次消费位置 => 随后拉取Message => 最后判断如果消费不为空则更新下标位置,这样的话在同一个事务内, 如果处理抛出异常则不变更消费位置
4. 新增MainExample测试
   - producerClient() 模拟生产者
   - consumerClient() 模拟消费者, 同时使用线程,开启2个线程执行同步消费,且互相不影响

#### 实现代码:
[点击](https://github.com/EwenSheng/JAVA-000/tree/main/Week_14/syw-mq)

#### 思考

1. producer并未实现同步/异步机制,如何保证消息一定发布成功
2. consumer未实现消息位置持久化,若程序关闭或异常终止则消息位置就会丢失
3. 内存中的队列随时会OOM,如何让数据真正的出队,循环队列？

### version 3.0 -- 设计ing

#### 设计思路:

1. 新建broker-server项目,迁移v2.server包下的代码移植到该项目内,拟态消息服务
2. 新建producer-client项目,迁移v2.producer包下的代码移植到该项目内,拟态"生产者"
   - 通过RestTemplate调用新建broker-server项目create接口,创建Topic
   - 通过RestTemplate调用新建broker-server项目的send接口生产消息
3. 新建consumer-client项目,迁移v2.consumer包下的代码移植到该项目内,拟态“消费者”
   - 通过RestTemplate调用新建broker-server项目sub接口,订阅Topic
   - 通过RestTemplate调用新建broker-server项目的poll接口消费消息   