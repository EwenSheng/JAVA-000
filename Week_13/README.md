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
