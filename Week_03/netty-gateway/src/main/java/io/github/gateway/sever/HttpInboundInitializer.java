package io.github.gateway.sever;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private String proxyServer;

    public HttpInboundInitializer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    /**
     * @author: Ewen
     * @date: 2020/11/2 14:24
     * @param: [ch]
     * @return: void
     * @description: 获得通道channel中的管道链（执行链、handler链）
     */
    @Override
    public void initChannel(SocketChannel ch) {

        ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new HttpInboundHandler(this.proxyServer));

		/*if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}*/

        /*p.addLast(new HttpServerExpectContinueHandler());*/
    }
}
