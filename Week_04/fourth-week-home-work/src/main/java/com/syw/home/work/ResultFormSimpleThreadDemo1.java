package com.syw.home.work;

import com.syw.home.work.entity.Result;

import java.util.Objects;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/8 20:14
 * @description:
 */
public class ResultFormSimpleThreadDemo1 {

    public static void main(String[] args) {

        Result result = new Result();

        Thread thread = new Thread(() -> result.setMessage("I'm the child thread call message;[" + Thread.currentThread().getName() + "]"));

        try {
            thread.start();

            while (Objects.isNull(result.getMessage())) {
                Thread.sleep(100L);
            }

            System.out.println(result.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
