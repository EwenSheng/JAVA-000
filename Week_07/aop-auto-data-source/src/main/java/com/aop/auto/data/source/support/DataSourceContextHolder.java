package com.aop.auto.data.source.support;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/12/5 20:17
 * @description:
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> DYNAMIC_DATASOURCE_CONTEXT = new ThreadLocal<>();

    public static void set(String datasourceType) {
        DYNAMIC_DATASOURCE_CONTEXT.set(datasourceType);
    }

    public static String get() {
        return DYNAMIC_DATASOURCE_CONTEXT.get();
    }

    public static void clear() {
        DYNAMIC_DATASOURCE_CONTEXT.remove();
    }
}
