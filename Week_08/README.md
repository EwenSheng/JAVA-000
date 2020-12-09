## 题目

### （周四）

1. （选做）分析前面作业设计的表，是否可以做垂直拆分。
2. （必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。
3. （选做）模拟 1000 万的订单单表数据，迁移到上面作业 2 的分库分表中。
4. （选做）重新搭建一套 4 个库各 64 个表的分库分表，将作业 2 中的数据迁移到新分库。

### （周六）：

1. （选做）列举常见的分布式事务，简单分析其使用场景和优缺点。
2. （必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。
3. （选做）基于 ShardingSphere narayana XA 实现一个简单的分布式事务 demo。
4. （选做）基于 seata 框架实现 TCC 或 AT 模式的分布式事务 demo。
5. （选做☆）设计实现一个简单的 XA 分布式事务框架 demo，只需要能管理和调用 2 个 MySQL 的本地事务即可，不需要考虑全局事务的持久化和恢复、高可用等。
6. （选做☆）设计实现一个 TCC 分布式事务框架的简单 Demo，需要实现事务管理器，不需要实现全局事务的持久化和恢复、高可用等。
7. （选做☆）设计实现一个 AT 分布式事务框架的简单 Demo，仅需要支持根据主键 id 进行的单个删改操作的 SQL 或插入操作的事务。

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

### 准备工作

~~~
-- 新建数据库实例
docker run -p 3308:3306 --name docker_mysql_M_1 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7.32

-- 新建数据库 & 新建表
ds_0
- t_order_1 ~ t_order_15

ds_1
- t_order_1 ~ t_order_15

-- 文档
https://shardingsphere.apache.org/document/legacy/4.x/document/cn/features/transaction/function/2pc-xa-transaction/
~~~

~~~
坑： spring-boot-starter-parent version 2.0+ 后 *.properties 不允许 _ 语法

Description:

Configuration property name 'spring.shardingsphere.rules.sharding.sharding-algorithms.database_inline.props' is not valid:

    Invalid characters: '_'
    Reason: Canonical names should be kebab-case ('-' separated), lowercase alpha-numeric characters and must start with a letter

Action:

Modify 'spring.shardingsphere.rules.sharding.sharding-algorithms.database_inline.props' so that it conforms to the canonical names requirements.
~~~

### （必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

作业地址： https://github.com/EwenSheng/JAVA-000/tree/main/Week_08/syw-ss-xa-example/src/test/java

SQL地址： https://github.com/EwenSheng/JAVA-000/tree/main/Week_08/db

配置文件： https://github.com/EwenSheng/JAVA-000/tree/main/Week_08/config

~~~
-- 数据采样1000 insert
...
2020-12-09 20:56:36.362  INFO 43272 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO t_order(`user_id`, `status`) VALUES(?, ?)
2020-12-09 20:56:36.362  INFO 43272 --- [           main] ShardingSphere-SQL                       : SQLStatement: InsertStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@39296cef, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@74ab8610), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@74ab8610, columnNames=[user_id, status], insertValueContexts=[InsertValueContext(parametersCount=2, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=48, stopIndex=48, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=51, stopIndex=51, parameterMarkerIndex=1), DerivedParameterMarkerExpressionSegment(super=ParameterMarkerExpressionSegment(startIndex=0, stopIndex=0, parameterMarkerIndex=2))], parameters=[5, 0])], generatedKeyContext=Optional[GeneratedKeyContext(columnName=id, generated=true, generatedValues=[543535645797122048])])
2020-12-09 20:56:36.362  INFO 43272 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-1 ::: INSERT INTO t_order_0(`user_id`, `status`, id) VALUES(?, ?, ?) ::: [5, 0, 543535645797122048]
userId: 8,%2:0
2020-12-09 20:56:36.365  INFO 43272 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO t_order(`user_id`, `status`) VALUES(?, ?)
2020-12-09 20:56:36.365  INFO 43272 --- [           main] ShardingSphere-SQL                       : SQLStatement: InsertStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@39296cef, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@1be3a294), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@1be3a294, columnNames=[user_id, status], insertValueContexts=[InsertValueContext(parametersCount=2, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=48, stopIndex=48, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=51, stopIndex=51, parameterMarkerIndex=1), DerivedParameterMarkerExpressionSegment(super=ParameterMarkerExpressionSegment(startIndex=0, stopIndex=0, parameterMarkerIndex=2))], parameters=[8, 0])], generatedKeyContext=Optional[GeneratedKeyContext(columnName=id, generated=true, generatedValues=[543535645805510657])])
2020-12-09 20:56:36.365  INFO 43272 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-0 ::: INSERT INTO t_order_1(`user_id`, `status`, id) VALUES(?, ?, ?) ::: [8, 0, 543535645805510657]
userId: 2,%2:0
2020-12-09 20:56:36.368  INFO 43272 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO t_order(`user_id`, `status`) VALUES(?, ?)
2020-12-09 20:56:36.368  INFO 43272 --- [           main] ShardingSphere-SQL                       : SQLStatement: InsertStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@39296cef, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@115924ba), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@115924ba, columnNames=[user_id, status], insertValueContexts=[InsertValueContext(parametersCount=2, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=48, stopIndex=48, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=51, stopIndex=51, parameterMarkerIndex=1), DerivedParameterMarkerExpressionSegment(super=ParameterMarkerExpressionSegment(startIndex=0, stopIndex=0, parameterMarkerIndex=2))], parameters=[2, 0])], generatedKeyContext=Optional[GeneratedKeyContext(columnName=id, generated=true, generatedValues=[543535645818093568])])
2020-12-09 20:56:36.368  INFO 43272 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-0 ::: INSERT INTO t_order_0(`user_id`, `status`, id) VALUES(?, ?, ?) ::: [2, 0, 543535645818093568]
userId: 0,%2:0
2020-12-09 20:56:36.370  INFO 43272 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO t_order(`user_id`, `status`) VALUES(?, ?)
2020-12-09 20:56:36.370  INFO 43272 --- [           main] ShardingSphere-SQL                       : SQLStatement: InsertStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@39296cef, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@3f0b5619), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@3f0b5619, columnNames=[user_id, status], insertValueContexts=[InsertValueContext(parametersCount=2, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=48, stopIndex=48, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=51, stopIndex=51, parameterMarkerIndex=1), DerivedParameterMarkerExpressionSegment(super=ParameterMarkerExpressionSegment(startIndex=0, stopIndex=0, parameterMarkerIndex=2))], parameters=[0, 0])], generatedKeyContext=Optional[GeneratedKeyContext(columnName=id, generated=true, generatedValues=[543535645830676481])])
2020-12-09 20:56:36.370  INFO 43272 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-0 ::: INSERT INTO t_order_1(`user_id`, `status`, id) VALUES(?, ?, ?) ::: [0, 0, 543535645830676481]
========== OrderService.insert() ==========
StopWatch 'insert order': running time = 4166613500 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
4166613500  100%  
~~~

### （必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。

XA 跨库 两段式提交, 测试代码如下:

~~~
  @Test
    public void xaOperating() {
        orderService.xaOperating(Long.valueOf("543525333131853825"), Long.valueOf("543525333022801920"));
    }
~~~

测试结果如下:

~~~
2020-12-09 20:47:39.392  INFO 34880 --- [           main] ShardingSphere-SQL                       : Logic SQL: UPDATE t_order SET status = ? WHERE id = ?
2020-12-09 20:47:39.393  INFO 34880 --- [           main] ShardingSphere-SQL                       : SQLStatement: UpdateStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.UpdateStatement@4769537a, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@4504a4ed), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@4504a4ed)
2020-12-09 20:47:39.393  INFO 34880 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-0 ::: UPDATE t_order_1 SET status = ? WHERE id = ? ::: [1, 543525333131853825]
2020-12-09 20:47:39.393  INFO 34880 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-1 ::: UPDATE t_order_1 SET status = ? WHERE id = ? ::: [1, 543525333131853825]
2020-12-09 20:47:39.489  INFO 34880 --- [           main] ShardingSphere-SQL                       : Logic SQL: UPDATE t_order SET status = ? WHERE id = ?
2020-12-09 20:47:39.489  INFO 34880 --- [           main] ShardingSphere-SQL                       : SQLStatement: UpdateStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.UpdateStatement@4769537a, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@9dbb1d9), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@9dbb1d9)
2020-12-09 20:47:39.489  INFO 34880 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-0 ::: UPDATE t_order_0 SET status = ? WHERE id = ? ::: [1, 543525333022801920]
2020-12-09 20:47:39.490  INFO 34880 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-1 ::: UPDATE t_order_0 SET status = ? WHERE id = ? ::: [1, 543525333022801920]

java.lang.RuntimeException: STW
~~~