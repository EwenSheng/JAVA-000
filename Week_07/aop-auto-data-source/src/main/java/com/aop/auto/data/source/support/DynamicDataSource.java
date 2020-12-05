package com.aop.auto.data.source.support;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/12/5 20:16
 * @description:
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }
}
