package com.aop.auto.data.source.support;

import com.aop.auto.data.source.constant.DynamicDataSourceEnum;

import java.lang.annotation.*;

/**
 * @author Even
 * @title: value
 * @package DynamicDataSourceEnum
 * @description:
 * @date 2020/12/520:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSourceSelector {

    DynamicDataSourceEnum value() default DynamicDataSourceEnum.MASTER;

    boolean clear() default true;
}
