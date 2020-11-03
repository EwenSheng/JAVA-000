package io.github.gateway.utils;

import io.github.gateway.entity.AddressInfo;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/2 20:11
 * @description:
 */
public class IpUtils {

    public static AddressInfo getIpInfo(String href) {

        AddressInfo model = new AddressInfo();


        try {
            URL url = new URL(href);

            model.setPort(url.getPort());
            model.setHost(url.getHost());

            InetAddress inetAddress = InetAddress.getByName(model.getHost());

            model.setIp(inetAddress.getHostAddress());

        } catch (MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }

        return model;

    }


    public static int parsePort(String href) throws MalformedURLException {
        //java.net中存在的类
        URL url = new URL(href);
        // 端口号; 如果 href 中没有明确指定则为 -1
        int port = url.getPort();
        if (port < 0) {
            // 获取对应协议的默认端口号
            port = url.getDefaultPort();
        }
        return port;
    }

    public static String parseHost(String href) throws MalformedURLException {
        //
        URL url = new URL(href);
        // 获取 host 部分
        String host = url.getHost();
        return host;
    }

    public static String parseIp(String host) throws UnknownHostException {
        // 根据域名查找IP地址
        InetAddress inetAddress = InetAddress.getByName(host);
        // IP 地址
        String address = inetAddress.getHostAddress();
        return address;
    }
}
