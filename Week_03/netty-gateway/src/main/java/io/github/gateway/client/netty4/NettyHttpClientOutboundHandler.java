package io.github.gateway.client.netty4;

import io.github.gateway.entity.AddressInfo;
import io.github.gateway.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpClientOutboundHandler {

    private static Logger logger = LoggerFactory.getLogger(NettyHttpClientOutboundHandler.class);

    private AddressInfo addressInfo;

    public NettyHttpClientOutboundHandler(String backendUrl) {

        addressInfo = IpUtils.getIpInfo(backendUrl);
    }





/*    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        URI uri = new URI(backendUrl + "/api/hello");

        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0,
                HttpMethod.GET,
                uri.toASCIIString());

        request.headers()
                .set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION)
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        ctx.writeAndFlush(request);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpResponse) {

            FullHttpResponse response = (FullHttpResponse) msg;

            ByteBuf buf = response.content();

            String result = buf.toString(CharsetUtil.UTF_8);

            System.out.println("response -> " + result);
        }

        *//*if (msg instanceof HttpContent) {

            HttpContent content = (HttpContent) msg;

            ByteBuf buf = content.content();

            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));

            buf.release();
        }*//*
    }*/


}