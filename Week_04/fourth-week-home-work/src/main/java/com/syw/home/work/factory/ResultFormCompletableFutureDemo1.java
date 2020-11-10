package com.syw.home.work.factory;

import java.util.concurrent.CompletableFuture;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/9 22:18
 * @description:
 */
public class ResultFormCompletableFutureDemo1 {

    public static void main(String[] args) {

        StringBuffer stringBuffer = new StringBuffer();

        CompletableFuture
                .supplyAsync(() -> stringBuffer.append("I'm the child thread call message;[").append(Thread.currentThread().getName()))
                .thenAccept(str -> stringBuffer.append(",").append(Thread.currentThread().getName()).append("]"));

        System.out.println(stringBuffer.toString());


        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "I'm the child thread call message;[" + Thread.currentThread().getName();
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return Thread.currentThread().getName() + "]";
        }), (st1, str2) -> {
            return st1 + " " + str2;
        }).join();

        System.out.println(result);

    }
}