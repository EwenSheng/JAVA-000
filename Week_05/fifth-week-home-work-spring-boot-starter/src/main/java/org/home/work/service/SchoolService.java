package org.home.work.service;

import org.home.work.entity.School;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 16:27
 * @description:
 */
public class SchoolService {

    private School school;

    public SchoolService(School school) {

        this.school = school;
    }

    public void doIt() {

        school.ding();
        school.getKlass().dong();
    }
}
