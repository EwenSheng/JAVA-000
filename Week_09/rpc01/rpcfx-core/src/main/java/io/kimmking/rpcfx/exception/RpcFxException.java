package io.kimmking.rpcfx.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
abstract class RpcFxException extends RuntimeException {

    private String message;

    RpcFxException(ErrorContext errorContext, Throwable throwable) {
        super(errorContext.getMessage(), throwable);
        this.message = errorContext.getMessage();
    }

    RpcFxException(String message) {
        this.message = message;
    }
}