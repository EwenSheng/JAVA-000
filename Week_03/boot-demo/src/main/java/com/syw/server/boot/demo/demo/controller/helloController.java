package com.syw.server.boot.demo.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/2 19:14
 * @description:
 */
@RestController
@RequestMapping("/api")
public class helloController {

    @GetMapping("/hello")
    public String hello(@RequestHeader(name = "nio", defaultValue = "") String name) {

        System.out.println("hello my gateway , header:{" + name + "}");

        return "hello my gateway";
    }
}
