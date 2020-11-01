package io.github.kimmking.gateway;


import io.github.kimmking.gateway.inbound.HttpInboundServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerApplication {

    private final static String GATEWAY_NAME = "NIOGateway";
    private final static String GATEWAY_VERSION = "1.0.0";

    private static final Logger logger = LoggerFactory.getLogger(NettyServerApplication.class);

    public static void main(String[] args) {

        //  Kimmking:
        //  http://localhost:8888/api/hello  ==> gateway API
        //  http://localhost:8088/api/hello  ==> backend service


        // 网关入站端口(指定代理监听的端口)
        String proxyPort = System.getProperty("proxyPort", "8888");

        // 后端服务地址 , 使用gateway-server-0.0.1-SNAPSHOT.jar启动
        String proxyServer = System.getProperty("proxyServer", "http://localhost:8088");


        int port = Integer.parseInt(proxyPort);

        logger.info("{} {} starting...", GATEWAY_NAME, GATEWAY_VERSION);

        HttpInboundServer server = new HttpInboundServer(port, proxyServer);

        logger.info("{} {} started at http://localhost:{} for server:{}", GATEWAY_NAME, GATEWAY_VERSION, port, proxyServer);

        try {
            server.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
