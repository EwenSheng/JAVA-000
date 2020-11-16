package org.home.work.controller;

import org.home.work.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 20:17
 * @description:
 */
@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping(value = "/print")
    public String print() {

        schoolService.doIt();

        return "success";
    }
}
