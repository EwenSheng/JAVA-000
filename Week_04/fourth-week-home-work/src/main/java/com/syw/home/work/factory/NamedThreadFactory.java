package com.syw.home.work.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/8 16:41
 * @description: 线程工厂
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String namePrefix;

    //线程组
    private final ThreadGroup group;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public NamedThreadFactory(String namePrefix, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    public NamedThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + "-thread-" + atomicInteger.getAndIncrement(), 0);
        /*System.out.println("====> create name: " + t.getName());*/
        return t;
    }
}
