package org.home.work;

import org.home.work.entity.School;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 21:01
 * @description:
 */
public class GotBeanFormBeanConfigXml {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:BeanConfig.xml");

        classPathXmlApplicationContext.refresh();

        School school = (School) classPathXmlApplicationContext.getBean("school");

        System.out.println(school.toString());

    }
}
