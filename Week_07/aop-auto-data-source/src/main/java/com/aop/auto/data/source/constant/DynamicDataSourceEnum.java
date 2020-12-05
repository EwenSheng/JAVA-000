package com.aop.auto.data.source.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/12/5 20:11
 * @description:
 */
@Getter
@AllArgsConstructor
public enum DynamicDataSourceEnum {

    MASTER("master"),
    SLAVE("slave");

    private String dataSourceName;
}
