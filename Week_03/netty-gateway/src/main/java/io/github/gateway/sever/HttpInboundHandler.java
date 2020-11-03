package io.github.gateway.sever;

import io.github.gateway.client.okhttp.OkhttpOutboundHandler;
import io.github.gateway.sever.filter.HttpRequestFilter;
import io.github.gateway.sever.filter.impl.HttpRequestFilterHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;

    private OkhttpOutboundHandler handler;

    private HttpRequestFilter filter;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;

        handler = new OkhttpOutboundHandler(this.proxyServer);
        filter = new HttpRequestFilterHandler();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {

            FullHttpRequest request = (FullHttpRequest) msg;

            filter.filter(request, ctx);

            handler.handle(request, ctx);

            /*AddressInfo info = IpUtils.getIpInfo(this.proxyServer);

            new NettyHttpClient(info.getHost(), info.getPort(), request.uri(), ctx).connect();*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
