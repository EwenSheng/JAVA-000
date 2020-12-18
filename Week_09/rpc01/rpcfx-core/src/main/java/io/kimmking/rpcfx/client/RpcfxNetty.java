package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.netty.RpcfxNettyClient;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import okhttp3.MediaType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/18 16:56
 * @description:
 */
public class RpcfxNetty {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        return serviceClass.cast(getByteProxy(serviceClass, url));
    }

    private static <T> T getByteProxy(Class<T> serviceClass, String url)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .implement(serviceClass)
                .intercept(InvocationHandlerAdapter.of(new RpcfxNettyInvocationHandler(serviceClass, url)))
                .make();

        return (T) dynamicType.load(Rpcfx.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public static class RpcfxNettyInvocationHandler implements InvocationHandler {

        public static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;

        private final String url;

        public <T> RpcfxNettyInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

            RpcfxRequest request = new RpcfxRequest();

            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

            RpcfxResponse response = post(request, url);

            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) throws Exception {
            String reqJson = JSON.toJSONString(req);

            System.out.println("req json: " + reqJson);

            return RpcfxNettyClient.getInstance().getResponse(reqJson, url);
        }
    }
}
