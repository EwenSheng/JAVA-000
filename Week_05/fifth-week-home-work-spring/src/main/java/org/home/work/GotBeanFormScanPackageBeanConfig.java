package org.home.work;

import org.home.work.config.ScanPackageBeanConfig;
import org.home.work.entity.BodyInfo;
import org.home.work.service.BodyInfoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 21:12
 * @description:
 */
public class GotBeanFormScanPackageBeanConfig {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ScanPackageBeanConfig.class);

        BodyInfoService bodyInfoService = applicationContext.getBean(BodyInfoService.class);

        bodyInfoService.print(new BodyInfo());
    }
}
