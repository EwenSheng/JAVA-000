## 题目

### 第一节课

1. （必做）搭建ActiveMQ服务，基于JMS，写代码分别实现对于queue和topic的消息生产和消费，代码提交到github。
2. （选做）基于数据库的订单表，模拟消息队列处理订单：
    - 一个程序往表里写新订单，标记状态为未处理(status=0);
    - 另一个程序每隔100ms定时从表里读取所有status=0的订单，打印一下订单数据，然后改成完成status=1；
    - （挑战☆）考虑失败重试策略，考虑多个消费程序如何协作。
3. （选做）将上述订单处理场景，改成使用ActiveMQ发送消息处理模式。
4. （挑战☆☆）搭建ActiveMQ的network集群和master-slave主从结构。
5. （挑战☆☆☆）基于ActiveMQ的MQTT实现简单的聊天功能或者Android消息推送。
6. （挑战☆）创建一个RabbitMQ，用Java代码实现简单的AMQP协议操作。
7. （挑战☆☆）搭建RabbitMQ集群，重新实现前面的订单处理。
8. （挑战☆☆☆）使用Apache Camel打通上述ActiveMQ集群和RabbitMQ集群，实现所有写入到ActiveMQ上的一个队列q24的消息，自动转发到RabbitMQ。
9. （挑战☆☆☆）压测ActiveMQ和RabbitMQ的性能。

### 第二节课

1. （必做）搭建一个3节点Kafka集群，测试功能和性能；实现spring kafka下对kafka集群的操作，将代码提交到github。
2. （选做）安装kafka-manager工具，监控kafka集群状态。
3. （挑战☆）演练本课提及的各种生产者和消费者特性。
4. （挑战☆☆☆）Kafka金融领域实战：在证券或者外汇、数字货币类金融核心交易系统里，对于订单的处理，大概可以分为收单、定序、撮合、清算等步骤。其中我们一般可以用mq来实现订单定序，然后将订单发送给撮合模块。

- 收单：请实现一个订单的rest接口，能够接收一个订单Order对象；
- 定序：将Order对象写入到kafka集群的order.usd2cny队列，要求数据有序并且不丢失；
- 撮合：模拟撮合程序（不需要实现撮合逻辑），从kafka获取order数据，并打印订单信息，要求可重放,顺序消费,消息仅处理一次。

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

### 搭建ActiveMQ服务，基于JMS，写代码分别实现对于queue和topic的消息生产和消费，代码提交到github。

~~~
JMS 的全称是 Java Message Service，即 Java 消息服务。用于在两个应用程序之间，或分布式系统中发送消息，进行异步通信
它主要用于在生产者和消费者之间进行消息传递，生产者负责产生消息，而消费者负责接收消息
JMS 支持两种发送和接收消息的模型

P2P 模型
采用点到点的方式发送信息，P2P 模型是基于队列的，消息生产者发送消息到队列，消息消费者从队列中获取消息，P2P 每个消息只有一个消费者，发送者和接受者在时间上没有依赖性

Pub/Sub 模型
发布和订阅模型定义了如何向一个内容节点发布和订阅消息，这个内容节点称为 Topic（主题），主题可以认为是消息传递的中介，消息发布者将消息发布到某一个主题，消息订阅者则从主题中订阅消息
~~~

### 搭建ActiveMQ服务(通过Docker)

1. 拉取镜像并构建

~~~
1. docker pull webcenter/activemq -- 拉取ActiveMQ镜像
2. docker run -d --name activemq -p 61617:61616 -p 8162:8161 webcenter/activemq
* 8161是 web 页面管理端口（对外映射为8162）
* 61616是 activemq 的容器使用端口（映射为61617）
~~~

2. 访问ActiveMQ

~~~
http://localhost:8162/

admin/admin
~~~

3. 使用SpringBoot集成ActiveMQ,并简单构建 MyConsumer/MyProducer

引入ActiveMQ

~~~
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-activemq</artifactId>
    </dependency>
~~~

#### Queue模式

MyProducer

~~~
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * @author: Ewen
     * @date: 2021/1/9 20:38
     * @param: [destination, message]
     * @return: void
     * @description:  JmsMessagingTemplate是发送消息的工具类，封装了JmsTemplate
     */
    public void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }
~~~

MyConsumer

~~~
    @JmsListener(destination = "syw.queue")
    public void receiveQueue(String text) {
        System.out.println("receive queue , text: " + text);
    }
~~~

Queue模式,执行结果:

~~~
2021-01-09 20:45:47.380  INFO 4332 --- [           main] QueueTest                                : Starting QueueTest using Java 1.8.0_131 on Ewen-Sheng_CP with PID 4332 (started by 45399 in D:\WorkSpaceIdea\geek-school\job\JAVA-000\Week_13\syw-mq-demo)
2021-01-09 20:45:47.382  INFO 4332 --- [           main] QueueTest                                : No active profile set, falling back to default profiles: default
2021-01-09 20:45:48.550  INFO 4332 --- [           main] QueueTest                                : Started QueueTest in 1.587 seconds (JVM running for 2.847)
receive queue , text: hello,ActiveMQ (mode: queue)
~~~

#### Topic模式

MQConfig,Topic通过Bean方式注入

~~~
    @Bean
    public Topic topic() {
        return new ActiveMQTopic("syw.topic");
    }
~~~

MyProducer

~~~
    @Resource
    private Topic topic;

    public void sendTopic(String message) {
        jmsMessagingTemplate.convertAndSend(topic, message);
    }
~~~

MyConsumer

~~~
    @JmsListener(destination = "syw.topic", containerFactory = "topicListenerFactory")
    public void receiveTopic(String text) {
        System.out.println("receive topic , text: " + text);
    }
~~~

Topic模式,执行结果

~~~
2021-01-09 21:08:35.658  INFO 23908 --- [           main] QueueTest                                : Starting QueueTest using Java 1.8.0_131 on Ewen-Sheng_CP with PID 23908 (started by 45399 in D:\WorkSpaceIdea\geek-school\job\JAVA-000\Week_13\syw-mq-demo)
2021-01-09 21:08:35.660  INFO 23908 --- [           main] QueueTest                                : No active profile set, falling back to default profiles: default
2021-01-09 21:08:36.884  INFO 23908 --- [           main] QueueTest                                : Started QueueTest in 1.645 seconds (JVM running for 2.983)
receive topic , text: hello,ActiveMQ (mode: topic)
~~~

#### 如果想2个模式一起玩

MyConfig(同时pub-sub-domain: true 配置会失效)

~~~
    @Bean("queueListenerFactory")
    public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean("topicListenerFactory")
    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
~~~

执行结果

~~~
2021-01-09 21:14:42.118  INFO 8076 --- [           main] QueueTest                                : Starting QueueTest using Java 1.8.0_131 on Ewen-Sheng_CP with PID 8076 (started by 45399 in D:\WorkSpaceIdea\geek-school\job\JAVA-000\Week_13\syw-mq-demo)
2021-01-09 21:14:42.120  INFO 8076 --- [           main] QueueTest                                : No active profile set, falling back to default profiles: default
2021-01-09 21:14:43.245  INFO 8076 --- [           main] QueueTest                                : Started QueueTest in 1.498 seconds (JVM running for 2.658)
receive queue , text: hello,ActiveMQ testTogether (mode: queue)
receive topic , text: hello,ActiveMQ testTogether (mode: topic)
~~~

### 搭建一个3节点Kafka集群，测试功能和性能；实现spring kafka下对kafka集群的操作，将代码提交到github。

~~~
topic -> table
message -> row
partition -> sharding/partition
~~~

0. 查看环境Zookeeper

~~~
-- 查看对应IP:
docker inspect zookeeper
...
"Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "be4ed17a73a3a5d58141b2ff91b5bec0642224df5032101d1dd06fba21c26c31",
                    "EndpointID": "8f870aee095c3a57fd56063c30c9689faf53c4d15dde0a885ed04488ab7f772f",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
...
定位到IPAddress
~~~

1. 构建容器&启动

~~~
-- 容器命名Kafka1，指定了broker_id为0，端口号9092，zk地址为本机2181
docker run -d --name kafka1 -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=172.17.0.2:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://172.17.0.3:9092 -e KAFKA_LISTENERS=PLAINTEXT://172.17.0.3:9092 -t wurstmeister/kafka
889936aa598de398ca0bd668a884bbe7398eba9f2f6aae1d7ac7601275713af2

-- Kafka1 启动成功后确认Zookeeper上注册信息
~~~

2. 单机部署测试 (参考老师的PPT 执行命令)

~~~
-- 进入kafka1的容器中
docker exec -it kafka1 bin/bash
-- 
bash-4.4# bin/kafka-topics.sh --zookeeper 172.17.0.2:2181 --list
-- 发布Topic , 1个副本 , 4个分区(create完毕后,通过zoolytic 检查 brokers > topics > testk > partitions 下4个副本[0,1,2,3] )
bash-4.4# bin/kafka-topics.sh --zookeeper 172.17.0.2:2181 --create --topic testk --partitions 4 --replication-factor 1
Created topic testk.
--
bash-4.4# bin/kafka-topics.sh --zookeeper 172.17.0.2:2181 --describe --topic testk
Topic: testk    PartitionCount: 4       ReplicationFactor: 1    Configs:
        Topic: testk    Partition: 0    Leader: 0       Replicas: 0     Isr: 0
        Topic: testk    Partition: 1    Leader: 0       Replicas: 0     Isr: 0
        Topic: testk    Partition: 2    Leader: 0       Replicas: 0     Isr: 0
        Topic: testk    Partition: 3    Leader: 0       Replicas: 0     Isr: 0
        
-- 创建消费者,指定topic为testk
bin/kafka-console-consumer.sh --bootstrap-server 172.17.0.3:9092 --from-beginning --topic testk
-- 创建发布者,指定topic为testk
bin/kafka-console-producer.sh --bootstrap-server 172.17.0.3:9092 --topic testk

-- 测试消息发送
-- producer端
bash-4.4# bin/kafka-console-producer.sh --bootstrap-server 172.17.0.3:9092 --topic testk
>hello syw , it's your first demo
>^.^
>
-- consumer端
bash-4.4# bin/kafka-console-consumer.sh --bootstrap-server 172.17.0.3:9092 --from-beginning --topic testk
hello syw , it's your first demo
^.^
~~~

2.1 性能脚本测试(笔记)

~~~
-- producer端
bin/kafka-producer-perf-test.sh --topic testk --num-records 100000 --record-size 1000 --throughput 2000 --producer-props bootstrap.servers=172.17.0.3:9092
-- --num-records 测试数量
-- --record-size 每条记录大小（字节）
-- --throughput 流控每秒测试数量
-- consumer端
bin/kafka-consumer-perf-test.sh --bootstrap-server 172.17.0.3:9092 --topic testk --fetch-size 1048576 --messages 100000 --threads1
~~~

3. 构建集群

~~~
-- KAFKA_ADVERTISED_LISTENERS=[本地ipv4地址]
docker run -d --name kafka1 -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=172.17.0.2:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://10.51.4.183:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -e KAFKA_NUM_PARTITIONS=3 -e KAFKA_DEFAULT_REPLICATION_FACTOR=2 -t wurstmeister/kafka

docker run -d --name kafka2 -p 9093:9093 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=172.17.0.2:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://10.51.4.183:9093 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9093 -e KAFKA_NUM_PARTITIONS=3 -e KAFKA_DEFAULT_REPLICATION_FACTOR=2 -t wurstmeister/kafka

docker run -d --name kafka3 -p 9094:9094 -e KAFKA_BROKER_ID=2 -e KAFKA_ZOOKEEPER_CONNECT=172.17.0.2:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://10.51.4.183:9094 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9094 -e KAFKA_NUM_PARTITIONS=3 -e KAFKA_DEFAULT_REPLICATION_FACTOR=2 -t wurstmeister/kafka

-- 启动后，观察brokers > [0,1,2]

-- 发布Topic , 1个副本 , 4个分区(create完毕后,通过zoolytic 检查 brokers > topics > testk > partitions 下4个副本[0,1,2,3] )
-- 2副本 3分区
bin/kafka-topics.sh --zookeeper 172.17.0.2:2181 --create --topic test-3 --partitions 3 --replication-factor 2
bin/kafka-topics.sh --zookeeper 172.17.0.2:2181 --describe --topic test-3
--
Topic: test-3   PartitionCount: 3       ReplicationFactor: 2    Configs:
        Topic: test-3   Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
        Topic: test-3   Partition: 1    Leader: 0       Replicas: 0,2   Isr: 0,2
        Topic: test-3   Partition: 2    Leader: 1       Replicas: 1,0   Isr: 1,0
        
-- 测试消息发送
-- producer端
bin/kafka-console-producer.sh --bootstrap-server 172.17.0.3:9092,172.17.0.4:9093,172.17.0.5:9094 --topic test-33
-- 结果
bash-4.4# bin/kafka-console-producer.sh --bootstrap-server 172.17.0.3:9092,172.17.0.4:9093,172.17.0.5:9094 --topic test-33
>hello kafka group
>
-- consumer端
bin/kafka-console-consumer.sh --bootstrap-server 172.17.0.3:9092,172.17.0.4:9093,172.17.0.5:9094 --from-beginning --topic test-33
-- 结果
bash-4.4# bin/kafka-console-consumer.sh --bootstrap-server 172.17.0.3:9092,172.17.0.4:9093,172.17.0.5:9094 --from-beginning --topic test-33
hello kafka group
~~~

4. 通过spring-kafka 构建Producer端/Consumer端 结果:

Producer端: [项目地址](https://github.com/EwenSheng/JAVA-000/tree/main/Week_13/syw-kafka-demo)
Consumer端: [项目地址](https://github.com/EwenSheng/JAVA-000/tree/main/Week_13/syw-kafka-consumer)

~~~
POST http://localhost:13001/test/send

HTTP/1.1 200 
Content-Length: 0
Date: Tue, 12 Jan 2021 13:28:46 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>

Response code: 200; Time: 18ms; Content length: 0 bytes

-- Producer
...
2021-01-12 21:27:16.542  INFO 29760 --- [io-13001-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 62abe01bee039651
2021-01-12 21:27:16.542  INFO 29760 --- [io-13001-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1610458036537
2021-01-12 21:27:16.863  INFO 29760 --- [ad | producer-1] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: DvO1WMUpQcurF-iWZbXFTw
+++++++++++++++++++++  message = {"id":1610458126330,"msg":"syw4aa63c1b-0d2a-4764-a0a2-c451ee29f187","sendTime":"Jan 12, 2021 9:28:46 PM"}

-- Consumer
...
topic-idea-demo-0 to offset 0.
2021-01-12 21:28:02.174  INFO 8780 --- [ntainer#0-0-C-1] o.a.k.c.c.internals.SubscriptionState    : [Consumer clientId=consumer-ideal-consumer-group-1, groupId=ideal-consumer-group] Resetting offset for partition topic-idea-demo-1 to offset 2.
2021-01-12 21:28:02.194  INFO 8780 --- [ntainer#0-0-C-1] o.a.k.c.c.internals.SubscriptionState    : [Consumer clientId=consumer-ideal-consumer-group-1, groupId=ideal-consumer-group] Resetting offset for partition topic-idea-demo-2 to offset 0.
2021-01-12 21:28:02.218  INFO 8780 --- [ntainer#0-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : ideal-consumer-group: partitions assigned: [topic-idea-demo-1, topic-idea-demo-2, topic-idea-demo-0]
----------------- record =ConsumerRecord(topic = topic-idea-demo, partition = 1, leaderEpoch = 0, offset = 2, CreateTime = 1610458126331, serialized key size = 3, serialized value size = 105, headers = RecordHeaders(headers = [], isReadOnly = false), key = syw, value = {"id":1610458126330,"msg":"syw4aa63c1b-0d2a-4764-a0a2-c451ee29f187","sendTime":"Jan 12, 2021 9:28:46 PM"})
------------------ message ={"id":1610458126330,"msg":"syw4aa63c1b-0d2a-4764-a0a2-c451ee29f187","sendTime":"Jan 12, 2021 9:28:46 PM"}
~~~