package com.aop.auto.data.source.service.base;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;


public abstract class AbstractService<T, M extends BaseMapper<T>> {

    @Autowired
    private M mapper;

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    protected AbstractService() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = (Class<T>) type.getActualTypeArguments()[0];
    }

    protected EntityWrapper<T> createEntityWrapper() {
        return new EntityWrapper<>();
    }

    protected M getMapper() {
        return mapper;
    }
}