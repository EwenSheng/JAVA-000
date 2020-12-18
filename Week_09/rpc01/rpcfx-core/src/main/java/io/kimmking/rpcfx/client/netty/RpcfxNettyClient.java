package io.kimmking.rpcfx.client.netty;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/18 16:46
 * @description:
 */
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
