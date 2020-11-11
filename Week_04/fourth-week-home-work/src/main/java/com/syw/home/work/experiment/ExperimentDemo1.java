package com.syw.home.work.experiment;

import com.google.common.collect.Lists;
import com.syw.home.work.entity.Result;
import com.syw.home.work.factory.NamedThreadFactory;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/11 00:25
 * @description:
 * # 预计是做一个并发模型
 * # future.get() 会阻塞所以第一版模型构建失败了
 */
public class ExperimentDemo1 {

    public static void main(String[] args) {

        final ExecutorService service = Executors.newFixedThreadPool(4, new NamedThreadFactory("Demo"));

        /*final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);*/

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

        service.shutdown();
    }

    static class SendMessageHandler implements Callable<Result> {

        /*private CyclicBarrier cyclicBarrier;

        public SendMessageHandler(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }*/

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

                /*cyclicBarrier.await();*/

                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
