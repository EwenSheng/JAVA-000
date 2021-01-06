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

1. 创建属于redis的集群网络

0. 为什么要实现Redis Cluster
~~~
1.主从复制不能实现高可用
2.随着公司发展，用户数量增多，并发越来越多，业务需要更高的QPS，而主从复制中单机的QPS可能无法满足业务需求
3.数据量的考虑，现有服务器内存不能满足业务数据的需要时，单纯向服务器添加内存不能达到要求，此时需要考虑分布式需求，把数据分布到不同服务器上
4.网络流量需求：业务的流量已经超过服务器的网卡的上限值，可以考虑使用分布式来进行分流
5.离线计算，需要中间环节缓冲等别的需求
~~~
1. 创建属于redis的集群网络
~~~
PS D:\WorkSpaceDocker\Redis\Config> docker network create redis-cluster-net
022334c8fa752cb1779fdc971b24cc6a5d0e5665f50da8085188ad36da8e97e4
~~~
查看网关信息：172.18.0.1
~~~
PS D:\WorkSpaceDocker\Redis\Config> docker network inspect redis-cluster-net
[
    {
        "Name": "redis-cluster-net",
        "Id": "2db328899291110efd2b91aaffc06324d132e7f56dd68e55a0558293b906b53f",
        "Created": "2021-01-06T09:25:29.0189483Z",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": {},
            "Config": [
                {
                    "Subnet": "172.18.0.0/16",
                    "Gateway": "172.18.0.1"
                }
            ]
        },
        ...
]
~~~
2. 创建 7000~7005 端口的Redis配置文件,并且构建Redis镜像
~~~
-- 在windows环境下借助GitBash执行脚本

45399@Ewen-Sheng_CP MINGW64 /d/WorkSpaceDocker/Redis/Cluster
$ bash create.sh

docker run --name redis-7000 --net redis-cluster-net -d -p 7000:7000 -p 17000:17000 -v $PWD/7000/data:/data -v $PWD/7000/redis-7000.conf:/usr/local/etc/redis/redis.conf redis redis-server /usr/local/etc/redis/redis.conf
docker run --name redis-7001 --net redis-cluster-net -d -p 7001:7001 -p 17001:17001 -v $PWD/7001/data:/data -v $PWD/7001/redis-7001.conf:/usr/local/etc/redis/redis.conf redis redis-server /usr/local/etc/redis/redis.conf
docker run --name redis-7002 --net redis-cluster-net -d -p 7002:7002 -p 17002:17002 -v $PWD/7002/data:/data -v $PWD/7002/redis-7002.conf:/usr/local/etc/redis/redis.conf redis redis-server /usr/local/etc/redis/redis.conf
docker run --name redis-7003 --net redis-cluster-net -d -p 7003:7003 -p 17003:17003 -v $PWD/7003/data:/data -v $PWD/7003/redis-7003.conf:/usr/local/etc/redis/redis.conf redis redis-server /usr/local/etc/redis/redis.conf
docker run --name redis-7004 --net redis-cluster-net -d -p 7004:7004 -p 17004:17004 -v $PWD/7004/data:/data -v $PWD/7004/redis-7004.conf:/usr/local/etc/redis/redis.conf redis redis-server /usr/local/etc/redis/redis.conf
docker run --name redis-7005 --net redis-cluster-net -d -p 7005:7005 -p 17005:17005 -v $PWD/7005/data:/data -v $PWD/7005/redis-7005.conf:/usr/local/etc/redis/redis.conf redis redis-server /usr/local/etc/redis/redis.conf

-- 确认Redis
    PS D:\WorkSpaceDocker\Redis\Cluster> docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                                        NAMES
003e093d548e        redis               "docker-entrypoint.s…"   6 seconds ago       Up 4 seconds        0.0.0.0:7005->7005/tcp, 6379/tcp, 0.0.0.0:17005->17005/tcp   redis-7005
93b581b1bb04        redis               "docker-entrypoint.s…"   6 seconds ago       Up 5 seconds        0.0.0.0:7004->7004/tcp, 6379/tcp, 0.0.0.0:17004->17004/tcp   redis-7004
f2fcc5a141f2        redis               "docker-entrypoint.s…"   7 seconds ago       Up 5 seconds        0.0.0.0:7003->7003/tcp, 6379/tcp, 0.0.0.0:17003->17003/tcp   redis-7003
b6bbea23455b        redis               "docker-entrypoint.s…"   8 seconds ago       Up 6 seconds        0.0.0.0:7002->7002/tcp, 6379/tcp, 0.0.0.0:17002->17002/tcp   redis-7002
607b3affef97        redis               "docker-entrypoint.s…"   8 seconds ago       Up 7 seconds        0.0.0.0:7001->7001/tcp, 6379/tcp, 0.0.0.0:17001->17001/tcp   redis-7001
497dccb2382d        redis               "docker-entrypoint.s…"   9 seconds ago       Up 7 seconds        0.0.0.0:7000->7000/tcp, 6379/tcp, 0.0.0.0:17000->17000/tcp   redis-7000
~~~

3. 构建集群
~~~
-- 确认是否开启集群配置
docker exec -it redis-7000 redis-cli -p 7000 info cluster
-- 执行结果
PS D:\WorkSpaceDocker\Redis\Cluster> docker exec -it redis-7000 redis-cli -p 7000 info cluster
# Cluster
cluster_enabled:1

通过meet命令将其他实例，连接到集群上
docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.3 7001
docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.4 7002
docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.5 7003
docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.6 7004
docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.7 7005

-- 执行结果
PS D:\WorkSpaceDocker\Redis\Cluster> docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.3 7001
OK
PS D:\WorkSpaceDocker\Redis\Cluster> docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.4 7002
OK
PS D:\WorkSpaceDocker\Redis\Cluster> docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.5 7003
OK
PS D:\WorkSpaceDocker\Redis\Cluster> docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.6 7004
OK
PS D:\WorkSpaceDocker\Redis\Cluster> docker exec -it redis-7000 redis-cli -p 7000 cluster meet 172.18.0.7 7005
OK
-- 查看集群的节点信息
docker exec -it redis-7000 redis-cli -p 7000 cluster nodes

49b7e18bee441da09517eead72d21d99307a92ca 172.18.0.6:7004@17004 master - 0 1609932289000 3 connected
b89b1eae9957d3f41d3cf8ca719c097895bbed8b 172.18.0.5:7003@17003 master - 0 1609932290000 2 connected
6dcb219fe2f5923e843b11a45086b89fecd55113 172.18.0.7:7005@17005 master - 0 1609932291069 5 connected
ee511f51eb6dcf532f85748596ddc50d4500cf57 172.18.0.4:7002@17002 master - 0 1609932289063 0 connected
be76513824184611428ae045fcfac2d557615d68 172.18.0.2:7000@17000 myself,master - 0 1609932290000 1 connected
5078e74e6030205449ecc8767ab4b3ac9f03098c 172.18.0.3:7001@17001 master - 0 1609932290067 4 connected
~~~

4. 设置主从结构 3主3从
~~~
# 设置7001节点为7000节点的从节点
docker exec -it redis-7001 redis-cli -p 7001 cluster replicate ebd5e1bdcde72b413466dcb261c766ade7c3cc24 # 7001 --> 7000
# 设置7003节点为7002节点的从节点
docker exec -it redis-7003 redis-cli -p 7003 cluster replicate 58600a2a49a3c787309d6528d831dd457a65e121 # 7003 --> 7002
# 设置7005节点为7004节点的从节点
docker exec -it redis-7005 redis-cli -p 7005 cluster replicate e99607e64594d5da7065e03e3596d9ddcbd4c919 # 7005 --> 7004

-- 查看节点信息
docker exec -it redis-7000 redis-cli -p 7000 cluster nodes

9596c04f61832b9e0f810f24dcd64bd03f842031 172.18.0.5:7003@17003 master - 0 1609937506079 31 connected
ccd4aa1cd19c86eab2eeb041136087d5da6f524d 172.18.0.7:7005@17005 master - 0 1609937504000 39 connected 
58600a2a49a3c787309d6528d831dd457a65e121 172.18.0.4:7002@17002 master - 0 1609937504074 34 connected
d88b03704397bbc30f836dc0ce9f00c445c7243b 172.18.0.3:7001@17001 master - 0 1609937507081 37 connected 
e99607e64594d5da7065e03e3596d9ddcbd4c919 172.18.0.6:7004@17004 master - 0 1609937506000 38 connected
ebd5e1bdcde72b413466dcb261c766ade7c3cc24 172.18.0.2:7000@17000 myself,master 
~~~

5. 虚拟槽分区
~~~
- redis-cli -h 服务器IP -p 端口号 cluster addslots {0..5460}
- 通过cluster addslots方式，我们手动的指定每个主节点所分配的槽
docker exec -it redis-7000 redis-cli -p 7000 cluster addslots {0..5460}
docker exec -it redis-7002 redis-cli -p 7002 cluster addslots {5461..10920}
docker exec -it redis-7004 redis-cli -p 7004 cluster addslots {10921..16383}

docker exec -it redis-7000 redis-cli --cluster fix 172.18.0.2:7000

-- 移除节点下所有槽
docker exec -it redis-7000 redis-cli -p 7000 CLUSTER FLUSHSLOTS
docker exec -it redis-7001 redis-cli -p 7001 CLUSTER FLUSHSLOTS
docker exec -it redis-7002 redis-cli -p 7002 CLUSTER FLUSHSLOTS
docker exec -it redis-7003 redis-cli -p 7003 CLUSTER FLUSHSLOTS
docker exec -it redis-7004 redis-cli -p 7004 CLUSTER FLUSHSLOTS
docker exec -it redis-7005 redis-cli -p 7005 CLUSTER FLUSHSLOTS
~~~

6. 查询槽信息
~~~
127.0.0.1:7000> cluster slots

...
4685) 1) (integer) 16323
      2) (integer) 16323
      3) 1) "172.18.0.3"
         2) (integer) 7001
         3) "d88b03704397bbc30f836dc0ce9f00c445c7243b"
4686) 1) (integer) 16349
      2) (integer) 16349
      3) 1) "172.18.0.3"
         2) (integer) 7001
         3) "d88b03704397bbc30f836dc0ce9f00c445c7243b"
4687) 1) (integer) 16354
      2) (integer) 16354
      3) 1) "172.18.0.3"
         2) (integer) 7001
         3) "d88b03704397bbc30f836dc0ce9f00c445c7243b"
4688) 1) (integer) 16361
      2) (integer) 16361
      3) 1) "172.18.0.3"
         2) (integer) 7001
         3) "d88b03704397bbc30f836dc0ce9f00c445c7243b"
...
~~~


~~~
命令说明：
-p 6379:6379 : 将容器的 6379 端口映射到主机的 6379 端口
-v $PWD/data:/data : 将主机中当前目录下的 data 挂载到容器的 / data
redis-server --appendonly yes : 在容器执行 redis-server 启动命令，并打开 redis 持久化配置
~~~