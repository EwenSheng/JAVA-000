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

### 结合 dubbo+hmily,实现一个 TCC 外汇交易处理; ==> 未实现

#### 用户 A 的美元账户和人民币账户都在 A 库,使用 1 美元兑换 7 人民币;
#### 用户 B 的美元账户和人民币账户都在 B 库,使用 7 人民币兑换 1 美元;
#### 设计账户表,冻结资产表,实现上述两个本地事务的分布式事务。