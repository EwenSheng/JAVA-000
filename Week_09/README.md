## 题目

### （周四）

1. （选做）实现简单的 Protocol Buffer/Thrift/gRPC(选任一个) 远程调用 demo。
2. （选做）实现简单的 WebService-Axis2/CXF 远程调用 demo。
3. （必做）改造自定义 RPC 的程序，提交到 GitHub：

- 尝试将服务端写死查找接口实现类变成泛型和反射；
- 尝试将客户端动态代理改成其他字节码生成技术，添加异常处理；
- 尝试使用 Netty+HTTP 作为 client 端传输方式。

4. （选做☆☆））升级自定义 RPC 的程序：

- 尝试使用压测并分析优化 RPC 性能；
- 尝试使用 Netty+TCP 作为两端传输方式；
- 尝试自定义二进制序列化；
- 尝试压测改进后的 RPC 并分析优化，有问题欢迎群里讨论；
- 尝试将 fastjson 改成 xstream；
- 尝试使用字节码生成方式代替服务端反射。

### （周六）

1. （选做）按课程第二部分练习各个技术点的应用。
2. （选做）按 dubbo-samples 项目的各个 demo 学习具体功能使用。
3. （必做）结合 dubbo+hmily，实现一个 TCC 外汇交易处理，代码提交到 GitHub:

- 用户 A 的美元账户和人民币账户都在 A 库，使用 1 美元兑换 7 人民币 ;
- 用户 B 的美元账户和人民币账户都在 B 库，使用 7 人民币兑换 1 美元 ;
- 设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。

4. （挑战☆☆）尝试扩展 Dubbo

- 基于上次作业的自定义序列化，实现 Dubbo 的序列化扩展 ;
- 基于上次作业的自定义 RPC，实现 Dubbo 的 RPC 扩展 ;
- 在 Dubbo 的 filter 机制上，实现 REST 权限控制，可参考 dubbox;
- 实现一个自定义 Dubbo 的 Cluster/Loadbalance 扩展，如果一分钟内调用某个服务 / 提供者超过 10 次，则拒绝提供服务直到下一分钟 ;
- 整合 Dubbo+Sentinel，实现限流功能 ;
- 整合 Dubbo 与 Skywalking，实现全链路性能监控。

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

### 改造自定义 RPC 的程序

#### 尝试将服务端写死查找接口实现类变成泛型和反射;

1. 思路：Server端在容器启动时,把提供Rpc的接口Class加载的全局Map中,接收到Http时,通过Search Map中的class来定位接口
2. 改造代码部分

- 容器启动时加载类进全局Map,[链接][https://github.com/EwenSheng/JAVA-000/tree/main/Week_09/rpc01/rpcfx-demo-provider/src/main/java/io/kimmking/rpcfx/demo/provider/factory]
- 查找接口class,[链接][https://github.com/EwenSheng/JAVA-000/blob/main/Week_09/rpc01/rpcfx-core/src/main/java/io/kimmking/rpcfx/server/RpcfxInvoker.java]

#### 尝试将客户端动态代理改成其他字节码生成技术,添加异常处理;

1. 思路：使用ByteBuddy替换Proxy,利用字节码生成代理类技术
2. 参考文档:

~~~
https://zhuanlan.zhihu.com/p/151843984
https://www.cnblogs.com/sea520/archive/2004/01/13/12230802.html
~~~

3. 改造代码部分

~~~
private static <T> T getByteProxy(Class<T> serviceClass, String url)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    // 替换成ByteBuddy
    // .subclass 创建一个继承至 Object 类型的类
    // .implement 为创建的类实现接口
    // .intercept 拦截并设定类的返回值
    // .make 委托函数
     DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
             .subclass(Object.class)
             .name(serviceClass.getCanonicalName())
             .implement(serviceClass)
             .intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(serviceClass, url)))
             .make();

    return (T) dynamicType.load(Rpcfx.class.getClassLoader())
            .getLoaded()
            .getDeclaredConstructor()
            .newInstance();
}
~~~

#### 尝试使用 Netty+HTTP 作为 client 端传输方式;

1. 思路：替换OkHttp部分 -> netty
2. 改造代码部分，[链接][https://github.com/EwenSheng/JAVA-000/tree/main/Week_09/rpc01/rpcfx-core/src/main/java/io/kimmking/rpcfx/client/netty]

~~~
public class RpcfxNettyClient {

    private final static RpcfxNettyClient INSTANCE = new RpcfxNettyClient();

    public static RpcfxNettyClient getInstance() {
        return INSTANCE;
    }

    /* 缓存的作用，使用Map来保存用过的Channel */
    public final static ConcurrentHashMap<String, Channel> channelPool = new ConcurrentHashMap<>();

    private final EventLoopGroup clientGroup;

    private RpcfxNettyClient() {
        clientGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder()
                .setNameFormat("rpc-netty-pool-%d").build(), SelectorProvider.provider());
    }

    public RpcfxResponse getResponse(String rpcRequest, String url)
            throws InterruptedException, URISyntaxException {

        FullHttpRequest fullHttpRequest = createFullHttpRequest(rpcRequest, new URI(url));

        URI uri = new URI(url);
        String cacheKey = uri.getHost() + ":" + uri.getPort();

        if (channelPool.containsKey(cacheKey)) {
            Channel channel = channelPool.get(cacheKey);
            try {
                RpcfxNettyInboundHandler handler = new RpcfxNettyInboundHandler();
                channel.pipeline().replace("clientHandler", "clientHandler", handler);
                channel.writeAndFlush(fullHttpRequest).sync();
                return handler.getResponse(3, TimeUnit.SECONDS);
            } catch (Exception e) {
                channel.close();
                channelPool.remove(cacheKey);
            }
        }

        RpcfxNettyInboundHandler handler = new RpcfxNettyInboundHandler();

        Channel channel = createChannel(uri.getHost(), uri.getPort());
        channel.pipeline().replace("clientHandler", "clientHandler", handler);
        channel.writeAndFlush(fullHttpRequest).sync();

        channelPool.put(cacheKey, channel);
        return handler.getResponse(3, TimeUnit.SECONDS);
    }

    private FullHttpRequest createFullHttpRequest(String content, URI uri) {

        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes(Charsets.UTF_8))
        );

        request.headers()
                .set(HttpHeaderNames.HOST, uri.getHost())
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes())
                .set(HttpHeaderNames.CONTENT_TYPE, "application/json");

        return request;
    }

    private Channel createChannel(String address, int port) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(clientGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.AUTO_CLOSE, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler((new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new HttpResponseDecoder())
                                .addLast(new HttpRequestEncoder())
                                .addLast("clientHandler", new RpcfxNettyInboundHandler());
                    }
                }));

        return bootstrap.connect(address, port).sync().channel();
    }

    public void destroy() {
        clientGroup.shutdownGracefully();
    }
}
~~~

### 结合 dubbo+hmily,实现一个 TCC 外汇交易处理; ==> 待完成

#### 准备工作

- 用户 A 的美元账户和人民币账户都在 A 库,使用 1 美元兑换 7 人民币;
- 用户 B 的美元账户和人民币账户都在 B 库,使用 7 人民币兑换 1 美元;
- 设计账户表,冻结资产表,实现上述两个本地事务的分布式事务。

1. 通过Docker安装Zookeeper
~~~
-- zooker last version
docker pull zookeeper
-- 注册 zookeeper 镜像
 docker run -d --name local_zookeeper -p 2181:2181 zookeeper
-- 启动
docker ps -a -- 查看id
docker start id/name
~~~

2. 参考文档

[dubbo 用户手册](http://dubbo.apache.org/zh/docs/v2.7/user/configuration/xml/)
[himly-dubbo 用户手册](https://dromara.org/website/zh-cn/docs/hmily/user-dubbo.html)
[himly-dubbo-demo 源码](https://github.com/dromara/hmily/tree/master/hmily-demo/hmily-demo-dubbo)
[shardingsphere 配置](https://shardingsphere.apache.org/document/legacy/4.x/document/cn/manual/sharding-jdbc/configuration/config-yaml/)

组装这些第三包及其痛苦,另外不知道是不是我的Idea问题,老发生properties update后ss无法读取到数据库type;
- spring-boot 2.x 以上使用 shardingshpare-jdbc 4.x 以上版本;
- dubbo 2.7.5 以上版本,zookeeper 最新版本;
- 一定要clone himly-demo下来看下!!!


3. 启动hmily本地配置,准备Hmily数据库


作业地址：
[地址](https://github.com/EwenSheng/JAVA-000/tree/main/Week_09/syw-rpc-tcc-example)

consumer:
~~~
test_a begin =>>>>>
test_a end =>>>>>
confirm =>>>>> userid:1,balance:1
~~~

provider:
~~~
USDAccountDTO operating =>>>>>USDAccountDTO(userId=1, balance=1, deduction=true)
2020-12-20 15:18:40.721  INFO 30536 --- [:20890-thread-2] ShardingSphere-SQL                       : Logic SQL: SELECT id,user_id,balance FROM usd_account WHERE user_id = ?
2020-12-20 15:18:40.721  INFO 30536 --- [:20890-thread-2] ShardingSphere-SQL                       : SQLStatement: SelectStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement@4c5cfdf1, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@1ca164cd), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@1ca164cd, projectionsContext=ProjectionsContext(startIndex=7, stopIndex=24, distinctRow=false, projections=[ColumnProjection(owner=null, name=id, alias=Optional.empty), ColumnProjection(owner=null, name=user_id, alias=Optional.empty), ColumnProjection(owner=null, name=balance, alias=Optional.empty)]), groupByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.groupby.GroupByContext@687305b6, orderByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.orderby.OrderByContext@11fee45b, paginationContext=org.apache.shardingsphere.sql.parser.binder.segment.select.pagination.PaginationContext@3a6620e0, containsSubquery=false)
2020-12-20 15:18:40.721  INFO 30536 --- [:20890-thread-2] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: SELECT id,user_id,balance FROM usd_account WHERE user_id = ? ::: [1]
USDAccountDTO cancel =>>>>>USDAccountDTO(userId=1, balance=1, deduction=true)
2020-12-20 15:18:40.769  INFO 30536 --- [ecutorHandler-5] ShardingSphere-SQL                       : Logic SQL: DELETE FROM usd_freeze_account WHERE user_id = ?
2020-12-20 15:18:40.769  INFO 30536 --- [ecutorHandler-5] ShardingSphere-SQL                       : SQLStatement: DeleteStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.DeleteStatement@496940b3, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@3d6ebcf0), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@3d6ebcf0)
2020-12-20 15:18:40.769  INFO 30536 --- [ecutorHandler-5] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: DELETE FROM usd_freeze_account WHERE user_id = ? ::: [1]
USDAccountDTO operating =>>>>>USDAccountDTO(userId=1, balance=1, deduction=true)
2020-12-20 15:19:32.844  INFO 30536 --- [:20890-thread-3] ShardingSphere-SQL                       : Logic SQL: SELECT id,user_id,balance FROM usd_account WHERE user_id = ?
2020-12-20 15:19:32.844  INFO 30536 --- [:20890-thread-3] ShardingSphere-SQL                       : SQLStatement: SelectStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement@4c5cfdf1, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@149c5b2d), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@149c5b2d, projectionsContext=ProjectionsContext(startIndex=7, stopIndex=24, distinctRow=false, projections=[ColumnProjection(owner=null, name=id, alias=Optional.empty), ColumnProjection(owner=null, name=user_id, alias=Optional.empty), ColumnProjection(owner=null, name=balance, alias=Optional.empty)]), groupByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.groupby.GroupByContext@1a5ba7ce, orderByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.orderby.OrderByContext@1e64305d, paginationContext=org.apache.shardingsphere.sql.parser.binder.segment.select.pagination.PaginationContext@5b66adf5, containsSubquery=false)
2020-12-20 15:19:32.844  INFO 30536 --- [:20890-thread-3] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: SELECT id,user_id,balance FROM usd_account WHERE user_id = ? ::: [1]
2020-12-20 15:19:32.873  INFO 30536 --- [:20890-thread-3] ShardingSphere-SQL                       : Logic SQL: INSERT INTO usd_freeze_account(`user_id`, `balance`, `account_id`) VALUES(?, ?, ?)
2020-12-20 15:19:32.873  INFO 30536 --- [:20890-thread-3] ShardingSphere-SQL                       : SQLStatement: InsertStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@65eae53a, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@36722007), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@36722007, columnNames=[user_id, balance, account_id], insertValueContexts=[InsertValueContext(parametersCount=3, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=74, stopIndex=74, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=77, stopIndex=77, parameterMarkerIndex=1), ParameterMarkerExpressionSegment(startIndex=80, stopIndex=80, parameterMarkerIndex=2)], parameters=[1, 1, 1])], generatedKeyContext=Optional.empty)
2020-12-20 15:19:32.873  INFO 30536 --- [:20890-thread-3] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: INSERT INTO usd_freeze_account(`user_id`, `balance`, `account_id`) VALUES(?, ?, ?) ::: [1, 1, 1]
USDAccountDTO confirm =>>>>>USDAccountDTO(userId=1, balance=1, deduction=true)
2020-12-20 15:19:32.881  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : Logic SQL: DELETE FROM usd_freeze_account WHERE user_id = ?
2020-12-20 15:19:32.881  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : SQLStatement: DeleteStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.DeleteStatement@496940b3, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@6763f21b), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@6763f21b)
2020-12-20 15:19:32.881  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: DELETE FROM usd_freeze_account WHERE user_id = ? ::: [1]
RMBAccountDTO operating =>>>>>RMBAccountDTO(userId=1, balance=7, deduction=false)
2020-12-20 15:19:32.889  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : Logic SQL: SELECT id,user_id,balance FROM usd_account WHERE user_id = ?
2020-12-20 15:19:32.889  INFO 30536 --- [:20890-thread-4] ShardingSphere-SQL                       : Logic SQL: SELECT id,user_id,balance FROM rmb_account WHERE user_id = ?
2020-12-20 15:19:32.889  INFO 30536 --- [:20890-thread-4] ShardingSphere-SQL                       : SQLStatement: SelectStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement@20570438, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@58a4959d), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@58a4959d, projectionsContext=ProjectionsContext(startIndex=7, stopIndex=24, distinctRow=false, projections=[ColumnProjection(owner=null, name=id, alias=Optional.empty), ColumnProjection(owner=null, name=user_id, alias=Optional.empty), ColumnProjection(owner=null, name=balance, alias=Optional.empty)]), groupByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.groupby.GroupByContext@589ed310, orderByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.orderby.OrderByContext@9a8e345, paginationContext=org.apache.shardingsphere.sql.parser.binder.segment.select.pagination.PaginationContext@1146408c, containsSubquery=false)
2020-12-20 15:19:32.889  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : SQLStatement: SelectStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement@4c5cfdf1, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@6b829fd7), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@6b829fd7, projectionsContext=ProjectionsContext(startIndex=7, stopIndex=24, distinctRow=false, projections=[ColumnProjection(owner=null, name=id, alias=Optional.empty), ColumnProjection(owner=null, name=user_id, alias=Optional.empty), ColumnProjection(owner=null, name=balance, alias=Optional.empty)]), groupByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.groupby.GroupByContext@424e8fdc, orderByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.orderby.OrderByContext@5671f1ad, paginationContext=org.apache.shardingsphere.sql.parser.binder.segment.select.pagination.PaginationContext@f052a88, containsSubquery=false)
2020-12-20 15:19:32.889  INFO 30536 --- [:20890-thread-4] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: SELECT id,user_id,balance FROM rmb_account WHERE user_id = ? ::: [1]
2020-12-20 15:19:32.889  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: SELECT id,user_id,balance FROM usd_account WHERE user_id = ? ::: [1]
2020-12-20 15:19:32.896  INFO 30536 --- [:20890-thread-4] ShardingSphere-SQL                       : Logic SQL: INSERT INTO rmb_freeze_account(`user_id`, `balance`, `account_id`) VALUES(?, ?, ?)
2020-12-20 15:19:32.896  INFO 30536 --- [:20890-thread-4] ShardingSphere-SQL                       : SQLStatement: InsertStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement@7fad9fa5, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@6ad56091), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@6ad56091, columnNames=[user_id, balance, account_id], insertValueContexts=[InsertValueContext(parametersCount=3, valueExpressions=[ParameterMarkerExpressionSegment(startIndex=74, stopIndex=74, parameterMarkerIndex=0), ParameterMarkerExpressionSegment(startIndex=77, stopIndex=77, parameterMarkerIndex=1), ParameterMarkerExpressionSegment(startIndex=80, stopIndex=80, parameterMarkerIndex=2)], parameters=[1, 7, 1])], generatedKeyContext=Optional.empty)
2020-12-20 15:19:32.896  INFO 30536 --- [:20890-thread-4] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: INSERT INTO rmb_freeze_account(`user_id`, `balance`, `account_id`) VALUES(?, ?, ?) ::: [1, 7, 1]
2020-12-20 15:19:32.897  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : Logic SQL: UPDATE usd_account SET balance = ? WHERE user_id = ?
2020-12-20 15:19:32.897  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : SQLStatement: UpdateStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.UpdateStatement@168917dd, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@189b73e4), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@189b73e4)
2020-12-20 15:19:32.897  INFO 30536 --- [ecutorHandler-6] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: UPDATE usd_account SET balance = ? WHERE user_id = ? ::: [0.00, 1]
RMBAccountDTO confirm =>>>>>RMBAccountDTO(userId=1, balance=7, deduction=false)
2020-12-20 15:19:32.905  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : Logic SQL: DELETE FROM rmb_freeze_account WHERE user_id = ?
2020-12-20 15:19:32.905  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : SQLStatement: DeleteStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.DeleteStatement@5aa4604e, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@5463856a), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@5463856a)
2020-12-20 15:19:32.905  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: DELETE FROM rmb_freeze_account WHERE user_id = ? ::: [1]
2020-12-20 15:19:32.912  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : Logic SQL: SELECT id,user_id,balance FROM rmb_account WHERE user_id = ?
2020-12-20 15:19:32.912  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : SQLStatement: SelectStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement@20570438, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@2401bb9b), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@2401bb9b, projectionsContext=ProjectionsContext(startIndex=7, stopIndex=24, distinctRow=false, projections=[ColumnProjection(owner=null, name=id, alias=Optional.empty), ColumnProjection(owner=null, name=user_id, alias=Optional.empty), ColumnProjection(owner=null, name=balance, alias=Optional.empty)]), groupByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.groupby.GroupByContext@14b451de, orderByContext=org.apache.shardingsphere.sql.parser.binder.segment.select.orderby.OrderByContext@7571701e, paginationContext=org.apache.shardingsphere.sql.parser.binder.segment.select.pagination.PaginationContext@1048a90f, containsSubquery=false)
2020-12-20 15:19:32.912  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: SELECT id,user_id,balance FROM rmb_account WHERE user_id = ? ::: [1]
2020-12-20 15:19:32.917  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : Logic SQL: UPDATE rmb_account SET balance = ? WHERE user_id = ?
2020-12-20 15:19:32.918  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : SQLStatement: UpdateStatementContext(super=CommonSQLStatementContext(sqlStatement=org.apache.shardingsphere.sql.parser.sql.statement.dml.UpdateStatement@2c10fd07, tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@71bc5a56), tablesContext=org.apache.shardingsphere.sql.parser.binder.segment.table.TablesContext@71bc5a56)
2020-12-20 15:19:32.918  INFO 30536 --- [ecutorHandler-7] ShardingSphere-SQL                       : Actual SQL: tcc-1 ::: UPDATE rmb_account SET balance = ? WHERE user_id = ? ::: [7.00, 1]
~~~

异常情况通过Exception回滚
~~~
java.lang.RuntimeException: org.dromara.hmily.common.exception.HmilyRuntimeException: 余额不足
org.dromara.hmily.common.exception.HmilyRuntimeException: 余额不足
~~~