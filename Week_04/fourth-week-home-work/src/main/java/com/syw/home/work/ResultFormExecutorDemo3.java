package com.syw.home.work;

import com.syw.home.work.factory.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/8 16:52
 * @description:
 */
public class ResultFormExecutorDemo3 {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("FutureFormFutureTaskDemo1")
        );

        FutureTask<String> futureTask = new FutureTask<>(() -> "I'm the child thread call message;[" + Thread.currentThread().getName() + "]");

        threadPoolExecutor.submit(futureTask);

        try {
            System.out.println("call back : [" + futureTask.get() + "]");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 手动关闭线程池
            threadPoolExecutor.shutdown();
        }
    }
}
