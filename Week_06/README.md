## 题目

###（周四）
1. （选做）尝试使用Lambda/Stream/Guava优化之前作业的代码。
2. （选做）尝试使用Lambda/Stream/Guava优化工作中编码的代码。
3. （选做）根据课上提供的材料，系统性学习一遍设计模式，并在工作学习中思考如何用设计模式解决问题。
4. （选做）根据课上提供的材料，深入了解Google和Alibaba编码规范，并根据这些规范，检查自己写代码是否符合规范，有什么可以改进的。


###（周六）：
1. （选做）：基于课程中的设计原则和最佳实践，分析是否可以将自己负责的业务系统进行数据库设计或是数据库服务器方面的优化。
2. （必做）：基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL的SQL文件到Github（后面2周的作业依然要是用到这个表结构）。
3. （选做）：尽可能多的从“常见关系数据库”中列的清单，安装运行，并使用上一题的SQL测试简单的增删改查。
4. （选做）：基于上一题，尝试对各个数据库测试100万订单数据的增删改查性能。
5. （选做）：尝试对MySQL不同引擎下测试100万订单数据的增删改查性能。
6. （选做）：模拟1000万订单数据，测试不同方式下导入导出（数据备份还原）MySQL的速度，包括jdbc程序处理和命令行处理。思考和实践，如何提升处理效率。
7. （选做）：对MySQL配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查100万次，对比性能，生成报告。

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


### 基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL的SQL文件到Github（后面2周的作业依然要是用到这个表结构）。

已完成，作业地址:

按需拆分为三大模块

用户模块:
~~~
本模块包含用户登录信息，用户个人信息CRUD

所以拆分成如下结构
account_info -- 账户信息表
account_logon_record -- 账户登录记录表
user_info -- 用户信息表
~~~

商品模块:
~~~
本模块包含商品上从 品牌 -> 类别 -> SPU -> SKU / 库存 -> 销售属性

product_brand -- 商品品牌表
product_category -- 商品类别表
product_spu -- 商品SPU表
product_sku -- 商品SKU表
product_sku_stock -- 商品SKU库存表
product_attr -- 商品销售key表
product_attr_properties -- 商品销售属性表
~~~

订单模块:

~~~
本模块包含订单模块从 下单 -> 支付 -> 发货 -> 回溯订单对应商品快照

order_info -- 订单信息表
order_logistics_info -- 订单物流发货信息表
order_logistics_flow -- 订单物流轨迹明细表
order_pay_info -- 订单支付表
order_product_snapshot_info -- 订单商品快照
~~~


综上所述感觉可以优化如下几点
1. 商品快照表因冗余在商品模块，这样可以减少订单查询商品块的查询时间复杂度，同时减少存储空间的浪费;
2. 支付信息可以单独拆一个模块出来，如是平台化系统支付应该是一个独立系统模块;
3. SKU / SPU 感觉设计太细，一开始用不着这么细致, 5.7+的版本可以用Json来代替一些复杂的数据字段维护;