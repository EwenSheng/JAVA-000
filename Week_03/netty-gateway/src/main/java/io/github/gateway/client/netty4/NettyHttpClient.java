package io.github.gateway.client.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;

public class NettyHttpClient {

    private String host;
    private Integer port;
    private String uri;
    private ChannelHandlerContext server;

    public NettyHttpClient(String host, Integer port, String uri, ChannelHandlerContext server) {
        this.host = host;
        this.port = port;
        this.uri = uri;
        this.server = server;
    }

    public void connect() {

        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {

            Bootstrap b = new Bootstrap();

            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                            ch.pipeline().addLast(new HttpResponseDecoder());
                            // 客户端发送的是http request，所以要使用HttpRequestEncoder进行编码
                            ch.pipeline().addLast(new HttpRequestEncoder());
                            ch.pipeline().addLast(new NettyHttpClientOutboundHandler2(server));
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(this.host, this.port).sync();

            URI uri = new URI(this.uri);

            DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_0,
                    HttpMethod.GET,
                    uri.toASCIIString());

            request.headers()
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();

            System.out.println("NettyHttpClient.connect -> channel.closeFuture.sync");

        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    /*public static void main(String[] args) throws Exception {
        NettyHttpClient client = new NettyHttpClient("localhost", 8088);
    }*/
}