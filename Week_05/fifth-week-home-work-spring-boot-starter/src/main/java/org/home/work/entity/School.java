package org.home.work.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * ConfigurationProperties注解就是让spring容器知道该配置类的配置项前缀是什么
 */
@Data
@ConfigurationProperties("school.service")
public class School {

    private Klass klass;

    private Student student;

    public void ding() {

        System.out.println(
                "Class1 have " + this.klass.getStudents().size() +
                        " students and one is " + this.student
        );
    }
}
