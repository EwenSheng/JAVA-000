package io.kimmking.rpcfx.demo.provider.factory;

import com.google.common.collect.Maps;
import io.kimmking.rpcfx.demo.provider.resolver.MyResolver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/15 22:51
 * @description:
 */

@Component
public class InitServiceBeanFactory implements CommandLineRunner {

    public static ConcurrentMap<Class<?>, Class<?>> SERVICE_MAPPING = Maps.newConcurrentMap();

    private static final String SCAN_PACKAGE_PATH = "io.kimmking.rpcfx.demo.provider.serivce.impl";

    @Override
    public void run(String... args) {

        SERVICE_MAPPING.putAll(MyResolver.initTargetClass(SCAN_PACKAGE_PATH));

        System.out.println("init service bean Factory working ...");
    }
}
