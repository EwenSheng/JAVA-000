package com.multiple.data.source.service.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    protected QueryWrapper<T> createEntityWrapper() {
        return new QueryWrapper<>();
    }

    protected M getMapper() {
        return mapper;
    }
}