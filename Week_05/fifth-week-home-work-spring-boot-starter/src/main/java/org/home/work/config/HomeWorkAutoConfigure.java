package org.home.work.config;

import org.home.work.service.SchoolService;
import org.home.work.entity.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 14:23
 * @description: ConditionalOnClass 条件判断的注解，表示对应的类在classpath目录下存在时，才会去解析对应的配置文件
 * EnableConfigurationProperties 注解给出了该配置类所需要的配置信息类
 */
@Configuration
@ConditionalOnClass(SchoolService.class)
@EnableConfigurationProperties(School.class)
public class HomeWorkAutoConfigure {

    public HomeWorkAutoConfigure() {
        System.out.println("begin auto configure");
    }


    @Autowired
    private School school;


    /***
     * 注解Bean 实例化一个 bean;
     *
     * 注解ConditionalOnMissingBean也是条件判断的注解，表示如果不存在对应的bean条件才成立 ;
     * 这里就表示如果已经有SchoolService的bean了，那么就不再进行该bean的生成;
     * 这个注解十分重要，涉及到默认配置和用户自定义配置的原理。也就是说用户可以自定义一个bean;
     * 这样的话，spring容器就不需要再初始化这个默认的bean了。
     *
     * 注解ConditionalOnProperty是条件判断的注解，表示如果配置文件中的响应配置项数值为true,才会对该bean进行初始化
     */
    @Bean
    @ConditionalOnMissingBean(SchoolService.class)
    @ConditionalOnProperty(prefix = "school.service", value = "enabled", havingValue = "true")
    SchoolService schoolService() {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:BeanConfig.xml");

        return new SchoolService(context.getBean(School.class));
    }

}
