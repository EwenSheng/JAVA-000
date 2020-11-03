package io.github.gateway.client.netty4;

import io.github.gateway.entity.ByteBufToBytes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.util.Objects;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/2 19:36
 * @description:
 */
public class NettyHttpClientOutboundHandler2 extends ChannelInboundHandlerAdapter {

    private ByteBufToBytes reader;

    private ChannelHandlerContext server;

    public NettyHttpClientOutboundHandler2(ChannelHandlerContext server) {
        this.server = server;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("异常：" + cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println("msg:" + msg);

        if (msg instanceof HttpResponse) {

            HttpResponse endPointResponse = (HttpResponse) msg;

            System.out.println("Client.channelRead => CONTENT_TYPE:" + endPointResponse.headers().get(HttpHeaderNames.CONTENT_TYPE));

            if (HttpUtil.isContentLengthSet(endPointResponse)) {

                reader = new ByteBufToBytes((int) HttpUtil.getContentLength(endPointResponse));

                System.out.println("reader isEnd:" + reader.isEnd());

                /*FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(endPointResponse));

                response.headers()
                        .set("Content-Type", "application/json")
                        .setInt("Content-Length", Integer.parseInt(Objects.requireNonNull(endPointResponse.headers().get(HttpHeaderNames.CONTENT_LENGTH))));*/

                server.flush();
            }
        }

        /*if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();

            if (reader.isEnd()) {
                System.out.println("Client.channelRead => Server said:" + new String(reader.readFull()));
                ctx.close();
            }
        }*/
    }
}
