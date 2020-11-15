package org.home.work;

import org.home.work.entity.UserInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/15 19:38
 * @description: # 通过applicationContext的 <bean>标签显式装配bean
 */
public class GotBeanFormApplicationContextXml {


    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserInfo userInfo = (UserInfo) context.getBean("userInfo");

        System.out.println(userInfo.toString());
    }
}
