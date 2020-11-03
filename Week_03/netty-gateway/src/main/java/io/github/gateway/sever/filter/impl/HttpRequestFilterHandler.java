package io.github.gateway.sever.filter.impl;

import io.github.gateway.entity.constant.ServerFilterHeaderConstant;
import io.github.gateway.sever.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.Objects;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/3 23:43
 * @description:
 */
public class HttpRequestFilterHandler implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {

        if (Objects.nonNull(fullRequest)) {

            HttpHeaders headers = fullRequest.headers();

            headers.add(ServerFilterHeaderConstant.NIO_KEY, "shengyiwen");
        }
    }
}
