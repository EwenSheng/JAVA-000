package io.kimmking.rpcfx.api;

import lombok.Data;

@Data
public class RpcfxResponse {

    private Object result;

    private boolean status = true;

    private String message;
}
