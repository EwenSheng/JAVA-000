package org.home.work.config;

import org.home.work.entity.JobInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/15 20:21
 * @description:
 */
@Configuration
@EnableAspectJAutoProxy
public class BeanConfig {


    @Bean
    public JobInfo jobInfo() {
        return new JobInfo();
    }
}
