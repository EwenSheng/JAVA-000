## 题目

### 第一节课

1. （必做）配置 redis 的主从复制，sentinel 高可用，Cluster 集群。
2. （选做）练习示例代码里下列类中的作业题: 08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java
3. （选做☆）练习 redission 的各种功能。
4. （选做☆☆）练习 hazelcast 的各种功能。
5. （选做☆☆☆）搭建 hazelcast 3 节点集群，写入 100 万数据到一个 map，模拟和演 示高可用。

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

~~~
docker pull redis:latest
~~~

### redis 主从复制

0. 主从复制
~~~
作用:
数据冗余:主从复制实现了数据的热备份，是持久化之外的一种数据冗余方式。
故障恢复:当主节点出现问题时，可以由从节点提供服务，实现快速的故障恢复;实际上是一种服务的冗余。
负载均衡:在主从复制的基础上，配合读写分离，可以由主节点提供写服务，由从节点提供读服务(即写Redis数据时应用连接主节点，读Redis数据时应用连接从节点) 分担服务器负载;尤其是在写少读多的场景下，通过多个从节点分担读负载，可以大大提高Redis服务器的并发量。
读写分离:可以用于实现读写分离，主库写、从库读，读写分离不仅可以提高服务器的负载能力，同时可根据需求的变化，改变从库的数量;
高可用基石
过程:
连接建立阶段
数据同步阶段
命令传播阶段
- 1. 保存主节点信息
- 2. 建立socket连接
- 3. 发送ping命令
- 4. 权限验证
- 5. 同步数据集
- 6. 命令持续复制
~~~

1. 创建3个Redis镜像
~~~
docker run --name redis-master -p 6379:6379 -d redis redis-server - 主
docker run --name redis-slave-1 -p 6380:6379 -d redis redis-server - 从1
docker run --name redis-slave-2 -p 6381:6379 -d redis redis-server - 从2
~~~

2. 确认3个redis镜像的内网IP
~~~
docker inspect redis-master
=> 172.17.0.2
docker inspect redis-slave-1
=> 172.17.0.4
docker inspect redis-slave-2
=> 172.17.0.3
~~~

3. 进入容器内部配置Redis,配置主从关系
~~~
先获取 CONTAINER ID
=> docker ps -a

944a5a918f4e        redis               "docker-entrypoint.s…"   9 minutes ago       Up 9 minutes                0.0.0.0:6379->6379/tcp              redis-master
8af7e096e16c        redis               "docker-entrypoint.s…"   10 minutes ago      Up 9 minutes                0.0.0.0:6381->6379/tcp              redis-slave-2
ceb54f51149e        redis               "docker-entrypoint.s…"   10 minutes ago      Up 9 minutes                0.0.0.0:6380->6379/tcp              redis-slave-1
==================================================================================================
-- 进入 docker 容器内部，查看当前 redis 角色（主 master 还是从 slave）（命令：info replication）
1. docker exec -it 944a5a918f4e redis-cli
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:0
master_replid:7a488ccd3fb30170f8ccb227a829a4df177ad047
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
==================================================================================================
2. docker exec -it 8af7e096e16c redis-cli
-- 设置主库关联
127.0.0.1:6379> SLAVEOF 172.17.0.2 6379
OK
-- 查看信息
# Replication
role:slave
master_host:172.17.0.2
master_port:6379
master_link_status:up
master_last_io_seconds_ago:7
master_sync_in_progress:0
slave_repl_offset:140
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:127d03912e36616e59823dce6e684d99ccdb4da9
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:140
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:140
==================================================================================================
3. docker exec -it ceb54f51149e redis-cli
-- 设置主库关联
127.0.0.1:6379> SLAVEOF 172.17.0.2 6379
OK
-- 查看信息
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:172.17.0.2
master_port:6379
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:98
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:127d03912e36616e59823dce6e684d99ccdb4da9
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:98
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:57
repl_backlog_histlen:42
~~~
 
4. 验证主从同步
~~~
-- 主库set
PS C:\Users\45399> docker exec -it 944a5a918f4e redis-cli
127.0.0.1:6379> set syw gogo
OK
127.0.0.1:6379> get syw
"gogo"
-- 从库1
PS C:\Users\45399> docker exec -it 8af7e096e16c redis-cli
127.0.0.1:6379> get syw
"gogo"
-- 从库2
PS C:\Users\45399> docker exec -it ceb54f51149e redis-cli
127.0.0.1:6379> get syw
"gogo"
~~~

| 容器名称 | 容器IP地址 | 映射端口号 | 服务运行模式 | CONTAINER ID |
| :----:  | :----:  | :----:  | :----:  | :----:  |
| redis-master | 172.17.0.2 | 6379 | master | 944a5a918f4e |
| redis-slave-1 | 172.17.0.4 | 6380 | slave | ceb54f51149e |
| redis-slave-2 | 172.17.0.3 | 6381 | slave | 8af7e096e16c |

### sentinel 哨兵高可用

1. 构建 sentinel.conf
~~~
# 禁止保护模式
protected-mode no
# 配置监听的主服务器，这里sentinel monitor代表监控，mymaster代表服务器的名称，可以自定义，192.168.11.128代表监控的主服务器，6379代表端口，2代表只有两个或两个以上的哨兵认为主服务器不可用的时候，才会进行failover操作。
sentinel monitor mymaster 172.17.0.2 6379 2
sentinel down-after-milliseconds master 5000
sentinel failover-timeout mymaster 180000
sentinel parallel-syncs mymaster 1
# sentinel author-pass定义服务的密码，mymaster是服务名称，123456是Redis服务器密码
# sentinel auth-pass <master-name> <password>
# sentinel auth-pass mymaster 123456
~~~

2. 创建3个Redis哨兵
~~~
docker run -it --name redis-sentinel-1 --net=host -v $PWD/sentinel-26379.conf:/usr/local/etc/redis/sentinel.conf -d redis redis-sentinel /usr/local/etc/redis/sentinel.conf
docker run -it --name redis-sentinel-2 --net=host -v $PWD/sentinel-26380.conf:/usr/local/etc/redis/sentinel.conf -d redis redis-sentinel /usr/local/etc/redis/sentinel.conf
docker run -it --name redis-sentinel-3 --net=host -v $PWD/sentinel-26381.conf:/usr/local/etc/redis/sentinel.conf -d redis redis-sentinel /usr/local/etc/redis/sentinel.conf
~~~

验证：
1. 观察3个哨兵节点启动日志
~~~
1:X 06 Jan 2021 06:21:42.780 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
1:X 06 Jan 2021 06:21:42.785 # Could not rename tmp config file (Device or resource busy)
1:X 06 Jan 2021 06:21:42.785 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
1:X 06 Jan 2021 06:21:42.785 # Sentinel ID is 8f856b8488fa826be073b61f11dc80f306206d6e
1:X 06 Jan 2021 06:21:42.785 # +monitor master mymaster 172.17.0.2 6379 quorum 2
1:X 06 Jan 2021 06:22:30.350 * +sentinel sentinel d7e3ce1f19cc0a9165eef70a87e1f41da2b82f2a 172.17.0.1 26380 @ mymaster 172.17.0.2 6379
1:X 06 Jan 2021 06:22:30.354 # Could not rename tmp config file (Device or resource busy)
1:X 06 Jan 2021 06:22:30.354 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
1:X 06 Jan 2021 06:23:04.837 * +sentinel sentinel 1aa3789d3c16f05691f923c1a3944aec47db9305 172.17.0.1 26381 @ mymaster 172.17.0.2 6379
1:X 06 Jan 2021 06:23:04.842 # Could not rename tmp config file (Device or resource busy)
1:X 06 Jan 2021 06:23:04.842 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
~~~
2. 关闭redis-master通过info replication查询主从变更
~~~
1. 主库(原slave)
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=172.17.0.4,port=6379,state=online,offset=27710,lag=1
master_replid:aac6b2bde72e10914cbd99ceb2e4f06bb3d5d44a
master_replid2:ccff9dd04a91e4dfb42a61e2db08b6a5bc960a09
master_repl_offset:27710
second_repl_offset:16953
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:27710
2. 从库
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:172.17.0.3
master_port:6379
master_link_status:up
master_last_io_seconds_ago:1
master_sync_in_progress:0
slave_repl_offset:28250
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:aac6b2bde72e10914cbd99ceb2e4f06bb3d5d44a
master_replid2:ccff9dd04a91e4dfb42a61e2db08b6a5bc960a09
master_repl_offset:28250
second_repl_offset:16953
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:429
repl_backlog_histlen:27822
~~~
3. 重启redis-master,变成了slave角色
~~~
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:172.17.0.3
master_port:6379
master_link_status:up
master_last_io_seconds_ago:1
master_sync_in_progress:0
slave_repl_offset:57103
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:aac6b2bde72e10914cbd99ceb2e4f06bb3d5d44a
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:57103
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:51099
repl_backlog_histlen:6005
~~~
4. 通过 redis-slave-1(目前master角色) 测试主从关系
~~~
1. redis-slave-1(目前master角色)
127.0.0.1:6379> set test2 test2
OK
2. redis-master
127.0.0.1:6379> keys *
1) "test2"
2) "cgy"
3. redis-slave-2
127.0.0.1:6379> keys *
1) "test2"
2) "cgy"
~~~

至此验证ok,主从 + 哨兵模式部署完毕

| 容器名称 | 容器IP地址 | 映射端口号 | 服务运行模式 | CONTAINER ID |
| :----:  | :----:  | :----:  | :----:  | :----:  |
| redis-master | 172.17.0.2 | 6379 | master | 944a5a918f4e |
| redis-slave-1 | 172.17.0.4 | 6380 | slave | ceb54f51149e |
| redis-slave-2 | 172.17.0.3 | 6381 | slave | 8af7e096e16c |
| redis-sentinel-1 | - | 26379 | sentinel | de2b9db2fbb7 | 
| redis-sentinel-2 | - | 26380 | sentinel | a74530e56d15 | 
| redis-sentinel-3 | - | 26381 | sentinel | 122808edaabe |

### cluster 集群


~~~
命令说明：
-p 6379:6379 : 将容器的 6379 端口映射到主机的 6379 端口
-v $PWD/data:/data : 将主机中当前目录下的 data 挂载到容器的 / data
redis-server --appendonly yes : 在容器执行 redis-server 启动命令，并打开 redis 持久化配置
~~~