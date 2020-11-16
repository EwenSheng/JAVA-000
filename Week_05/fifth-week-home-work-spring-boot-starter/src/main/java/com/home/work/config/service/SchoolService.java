package com.home.work.config.service;

import com.home.work.config.properties.Klass;
import com.home.work.config.properties.School;
import com.home.work.config.properties.Student;

import java.util.ArrayList;

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

        school.setKlass(new Klass());
        school.getKlass().setStudents(new ArrayList<>());
        school.setStudent(new Student());

        school.ding();
        school.getKlass().dong();
    }
}
