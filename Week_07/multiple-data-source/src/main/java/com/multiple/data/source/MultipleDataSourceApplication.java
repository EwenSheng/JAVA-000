package com.multiple.data.source;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// (exclude = {DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
@MapperScan("com.multiple.data.source.dao.mapper")
@SpringBootApplication
public class MultipleDataSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultipleDataSourceApplication.class, args);
    }
}
