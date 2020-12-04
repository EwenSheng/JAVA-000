## 题目

###（周四）
1. （选做）用今天课上学习的知识，分析自己系统的 SQL 和表结构
2. （必做）按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率
3. （选做）按自己设计的表结构，插入 1000 万订单模拟数据，测试不同方式的插入效
4. （选做）使用不同的索引或组合，测试不同方式查询效率
5. （选做）调整测试数据，使得数据尽量均匀，模拟 1 年时间内的交易，计算一年的销售报表：销售总额，订单数，客单价，每月销售量，前十的商品等等（可以自己设计更多指标）
6. （选做）尝试自己做一个 ID 生成器（可以模拟 Seq 或 Snowflake）
7. （选做）尝试实现或改造一个非精确分页的程序

###（周六）：
1. （选做）配置一遍异步复制，半同步复制、组复制
2. （必做）读写分离 - 动态切换数据源版本 1.0
3. （必做）读写分离 - 数据库框架版本 2.0
4. （选做）读写分离 - 数据库中间件版本 3.0
5. （选做）配置 MHA，模拟 master 宕机
6. （选做）配置 MGR，模拟 master 宕机
7. （选做）配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构

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
```

## 解题(必做题)

### （必做）按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

#### 方法：存储过程

~~~
执行环境：阿里云-RDS
环境配置：8Core / 16G
数据库版本：MySQL 5.7
~~~

~~~
-- 批量插入订单数据进Order表  , start = 起点id , max_num 截止id 
create PROCEDURE `insert_order_info`(in start int(10),in max_num int(10))
begin
 declare i int default 0;
 /*把autocommit设置成0*/
 set autocommit= 0;
 repeat
 set i=i+1;
 insert into order_info(id,num,amount,account_info_id,is_deleted,create_by,create_time,version)
 values((start+i),unix_timestamp(now())+nextval('ORDER_NUM'),5.00,1,0,'syw-dev',curdate(),1);
 until i=max_num end repeat;
commit;
end;

-- sequence 表结构
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence`  (
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- sequence自增函数实现
CREATE FUNCTION `nextval`(seq_name VARCHAR(10)) RETURNS int(6)
    DETERMINISTIC
BEGIN  
				 DECLARE value INTEGER;  
				 SELECT current_value + increment INTO value  
                   FROM sequence  
                   WHERE name = seq_name; 
         IF value > 999999  THEN
						SET value = 1;
         END IF;
					
         UPDATE sequence  
                   SET current_value = value 
                   WHERE name = seq_name;  
         RETURN value; 
END;

~~~

#### 结果：
通过执行计划插入order_info表
样本1：10w数据
~~~
call insert_order_info(1,100000)
> OK
> 时间: 46.828s
~~~
样本2：100w数据
~~~
call insert_order_info(1,1000000)
> OK
> 时间: 421.248s
~~~

### （必做）读写分离 - 动态切换数据源版本 1.0

### （必做）读写分离 - 数据库框架版本 2.0