package io.kimmking.rpcfx.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum ErrorContext {

    //COMMON
    ILLEGAl_ACCESS_EXCEPTION_MESSAGE("没有访问权限"),
    INSATNTIATION_EXCETPION_MESSAGE("实例化异常"),
    INVOCATION_TARGET_EXCEPTION_MESSAGE("反射异常"),
    CLASS_NOT_FOUND_EXCEPTION_MESSAGE("找不到指定类异常"),

    ;

    @Getter
    private String message;
}