package provider;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 17:01
 * @description:
 */

@SpringBootApplication
@MapperScan("provider.dao.mapper")
@EnableDubbo
@DubboComponentScan(basePackages = "com.syw.rpc.example.api.service")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
