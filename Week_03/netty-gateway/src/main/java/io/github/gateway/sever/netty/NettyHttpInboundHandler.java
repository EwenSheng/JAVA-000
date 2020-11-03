package io.github.gateway.sever.netty;

import io.github.gateway.client.netty4.NettyHttpClient;
import io.github.gateway.entity.AddressInfo;
import io.github.gateway.utils.IpUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/2 20:20
 * @description:
 */
public class NettyHttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;

    public NettyHttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        AddressInfo info = IpUtils.getIpInfo(this.proxyServer);

        new NettyHttpClient(info.getHost(), info.getPort(), "/api/hello", ctx).connect();

        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


}
