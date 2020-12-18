package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        // 0. 替换动态代理
        // 参数：类加载器去加载代理对象 / 动态代理类需要实现的接口 / 动态代理方法在执行时，会调用h里面的invoke方法去执行(实现InvocationHandler)
        /*return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));*/

        return serviceClass.cast(getByteProxy(serviceClass, url));
    }

    private static <T> T getByteProxy(Class<T> serviceClass, String url)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        // 替换成ByteBuddy
        // .subclass 创建一个继承至 Object 类型的类
        // .implement 为创建的类实现接口
        // .intercept 拦截并设定类的返回值
        // .make 委托函数

        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .implement(serviceClass)
                .intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(serviceClass, url)))
                .make();

        return (T) dynamicType.load(Rpcfx.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public static class RpcfxInvocationHandler implements InvocationHandler {

        public static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
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

        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
            String reqJson = JSON.toJSONString(req);

            System.out.println("req json: " + reqJson);

            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSON_TYPE, reqJson))
                    .build();

            String respJson = new OkHttpClient()
                    .newCall(request)
                    .execute()
                    .body()
                    .string();

            System.out.println("resp json: " + respJson);

            return JSON.parseObject(respJson, RpcfxResponse.class);
        }
    }
}
