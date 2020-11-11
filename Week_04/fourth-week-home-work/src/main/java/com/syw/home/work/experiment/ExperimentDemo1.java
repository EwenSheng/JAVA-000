package com.syw.home.work.experiment;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/11 00:25
 * @description: # 想做一个并发模型
 * # future.get() 会阻塞所以第一版模型构建失败了
 * #
 * # 思路变更使用
 * # ForkJoinTask: 提供在任务中执行fork和join的机制。
 * # ForkJoinPool: 负责来做实现，包括工作窃取算法、管理工作线程和提供关于任务的状态以及他们的执行信息。
 */
public class ExperimentDemo1 {

    public static void main(String[] args) {

        final ForkJoinPool pool = new ForkJoinPool(10);


        SimpleTask task = new SimpleTask(1, 100);

        Future<Integer> future = pool.submit(task);

        try {
            System.out.println("result:" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /*final ExecutorService service = Executors.newFixedThreadPool(4, new NamedThreadFactory("Demo"));

         *//*final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);*//*

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        ArrayList<Result> datas = Lists.newArrayList();

        Future<Result> future;

        for (int index = 0; index < 20; index++) {

            future = service.submit(new SendMessageHandler(countDownLatch));

            try {
                datas.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println(datas.size());

        service.shutdown();*/
    }

    /*static class SendMessageHandler implements Callable<Result> {

     *//*private CyclicBarrier cyclicBarrier;

        public SendMessageHandler(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }*//*

        private CountDownLatch countDownLatch;

        public SendMessageHandler(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Result call() throws Exception {
            Result result = new Result(Thread.currentThread().getName() + "add -> Result");

            try {

                Thread.sleep(2000L);
                System.out.println(result.toString());

                *//*cyclicBarrier.await();*//*

                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        }
    }*/

    static class SimpleTask extends RecursiveTask<Integer> {

        /* 阈值 */
        private static final int MAX_LENGTH = 10;

        private int start;
        private int end;

        public SimpleTask(int start, int end) {
            this.start = start;
            this.end = end;
        }


        @Override
        protected Integer compute() {

            if ((end - start) <= MAX_LENGTH) {

                int total = 0;

                for (int index = start; index < end; index++) {
                    total += index;
                }

                return total;
            } else {
                SimpleTask upper = new SimpleTask(start, calculateEnd());

                upper.fork();

                SimpleTask lower = new SimpleTask(calculateStart() + 1, end);

                lower.fork();

                return upper.join() + lower.join();
            }
        }


        private int calculateStart() {
            return (start + end) / 2 + 1;
        }

        private int calculateEnd() {
            return (start + end) / 2;
        }
    }
}
