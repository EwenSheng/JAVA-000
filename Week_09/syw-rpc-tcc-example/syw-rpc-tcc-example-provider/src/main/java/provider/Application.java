package provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 17:01
 * @description:
 */
@MapperScan("provider.dao.mapper")
@ImportResource({"classpath:spring-dubbo.xml"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
