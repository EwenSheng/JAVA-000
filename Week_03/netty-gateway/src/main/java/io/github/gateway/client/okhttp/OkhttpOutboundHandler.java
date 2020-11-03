package io.github.gateway.client.okhttp;

import io.github.gateway.entity.constant.ServerFilterHeaderConstant;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {

    private static Logger logger = LoggerFactory.getLogger(OkhttpOutboundHandler.class);

    private String backendUrl;
    private OkHttpClient okHttpClient;

    public OkhttpOutboundHandler(String backendUrl) {

        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;

        // 构建OKHttpClinent
        // 设置超时时间2秒
        okHttpClient = new OkHttpClient
                .Builder()
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {

        final String url = this.backendUrl + fullRequest.uri();

        fetchGet(fullRequest, ctx, url);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * @author: Ewen
     * @date: 2020/11/1 15:07
     * @param: [inbound, ctx, url]
     * @return: void
     * @description: 使用OkHttp异步调用代理服务的接口 & 处理Response
     */
    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {

        Request request = new Request
                .Builder()
                .url(url)
                .header(ServerFilterHeaderConstant.NIO_KEY, inbound.headers().get(ServerFilterHeaderConstant.NIO_KEY))
                .build();

        okHttpClient
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        handleResponse(inbound, ctx, response);
                    }
                });
    }


    /**
     * @author: Ewen
     * @date: 2020/11/1 15:03
     * @param: [fullRequest, ctx, endPointResponse]
     * @return: void
     * @description: 处理被网关代理服务的Netty Response
     */
    private void handleResponse(final FullHttpRequest fullRequest,
                                final ChannelHandlerContext ctx,
                                final Response endPointResponse) {

        FullHttpResponse response = null;

        try {

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(endPointResponse.body().bytes()));

            response.headers()
                    .set("Content-Type", "application/json")
                    .setInt("Content-Length", Integer.parseInt(Objects.requireNonNull(endPointResponse.header("Content-Length"))));

        } catch (IOException e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }

            ctx.flush();
        }
    }

}
