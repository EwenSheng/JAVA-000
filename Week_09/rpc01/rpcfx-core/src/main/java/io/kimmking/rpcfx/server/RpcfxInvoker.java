package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcResponses;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.ErrorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

public class RpcfxInvoker {

    private RpcfxResolver resolver;

    private ConcurrentMap<Class<?>, Class<?>> serviceMap;

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxInvoker(RpcfxResolver resolver, final ConcurrentMap<Class<?>, Class<?>> serviceMap) {
        this.resolver = resolver;
        this.serviceMap = serviceMap;
    }

/*    public RpcfxResponse invoke(RpcfxRequest request) {

        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
        Object service = resolver.resolve(serviceClass);
        // 包名.类名 io.kimmking.rpcfx.demo.api.UserService

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }*/

    public RpcfxResponse invokeNew(RpcfxRequest request) {

        try {

            Class<?> service = serviceMap.get(Class.forName(request.getServiceClass()));

            Method method = resolveMethodFromClass(service, request.getMethod());

            Object result = method.invoke(service.newInstance(), request.getParams());

            return RpcResponses.create(JSON.toJSONString(result, SerializerFeature.WriteClassName));

        } catch (IllegalAccessException e) {
            return RpcResponses.error(ErrorContext.ILLEGAl_ACCESS_EXCEPTION_MESSAGE.getMessage());
        } catch (InstantiationException e) {
            return RpcResponses.error(ErrorContext.INSATNTIATION_EXCETPION_MESSAGE.getMessage());
        } catch (InvocationTargetException e) {
            return RpcResponses.error(ErrorContext.INVOCATION_TARGET_EXCEPTION_MESSAGE.getMessage());
        } catch (ClassNotFoundException e) {
            return RpcResponses.error(ErrorContext.CLASS_NOT_FOUND_EXCEPTION_MESSAGE.getMessage());
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {

        return Arrays.stream(klass.getMethods())
                .filter(m -> methodName.equals(m.getName()))
                .findFirst()
                .orElse(null);
    }
}
