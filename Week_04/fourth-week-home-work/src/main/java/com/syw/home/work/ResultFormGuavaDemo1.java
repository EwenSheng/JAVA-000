package com.syw.home.work;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.syw.home.work.factory.NamedThreadFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/8 17:31
 * @description:
 */
public class ResultFormGuavaDemo1 {

    public static void main(String[] args) {

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(2, new NamedThreadFactory("Demo")));

        ListenableFuture<String> future = service.submit(() -> "I'm the child thread call message;[" + Thread.currentThread().getName() + "]");

        try {
            System.out.println("call back : [" + future.get() + "]");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }
}
