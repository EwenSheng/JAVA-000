package com.syw.home.work;

import com.syw.home.work.factory.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/8 16:35
 * @description: 手动创建线程池(参考newSingleThreadExecutor)并操作callable返回子线程结果
 */
public class ResultFromExecutorDemo2 {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("FutureFromExecutorDemo2")
        );

        Future<String> future = threadPoolExecutor.submit(() -> "I'm the child thread call message;[" + Thread.currentThread().getName() + "]");

        try {
            System.out.println("call back : [" + future.get() + "]");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 手动关闭线程池
            threadPoolExecutor.shutdown();
        }

    }
}
