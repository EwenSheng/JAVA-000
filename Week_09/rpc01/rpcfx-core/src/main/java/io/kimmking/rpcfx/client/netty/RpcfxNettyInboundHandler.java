package io.kimmking.rpcfx.client.netty;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.CharsetUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/18 16:46
 * @description:
 */
public class RpcfxNettyInboundHandler extends ChannelInboundHandlerAdapter {

    private RpcfxResponse response;

    private CountDownLatch latch;

    public RpcfxNettyInboundHandler() {
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            response = JSON.parseObject(buf.toString(CharsetUtil.UTF_8), RpcfxResponse.class);
            buf.release();
            latch.countDown();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public RpcfxResponse getResponse(long timeout, TimeUnit unit) throws InterruptedException {
        latch.await(3, TimeUnit.SECONDS);
        return response;
    }
}
