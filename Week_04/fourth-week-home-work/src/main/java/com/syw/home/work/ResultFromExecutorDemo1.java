package com.syw.home.work;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/8 15:55
 * @description: 通过Executors创建单线程-线程池并操作callable返回子线程结果
 */
public class ResultFromExecutorDemo1 {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> "I'm the child thread call message;[" + Thread.currentThread().getName() + "]");
        try {
            System.out.println("call back : [" + future.get() + "]");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 手动关闭线程池
            executorService.shutdown();
        }
    }
}
