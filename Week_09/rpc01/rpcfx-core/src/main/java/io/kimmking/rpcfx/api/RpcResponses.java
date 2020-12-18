package io.kimmking.rpcfx.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/17 17:02
 * @description:
 */
public final class RpcResponses {

    private static final String UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE = "静态工厂Results不支持实例化操作";

    private RpcResponses() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
    }

    public static RpcfxResponse kv(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);

        return create(data);
    }

    public static RpcfxResponse success() {
        return new RpcfxResponse();
    }

    public static RpcfxResponse error(String message) {
        RpcfxResponse response = new RpcfxResponse();
        response.setStatus(false);
        response.setMessage(message);

        return response;
    }

    public static RpcfxResponse create(Object data) {
        RpcfxResponse response = new RpcfxResponse();
        response.setStatus(true);
        response.setResult(data);

        return response;
    }
}
