package org.home.work;

import org.home.work.config.BeanConfig;
import org.home.work.entity.JobInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 21:06
 * @description:
 */
public class GotBeanFormBeanConfig {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);

        JobInfo jobInfo = applicationContext.getBean(JobInfo.class);

        System.out.println(jobInfo.toString());
    }
}
