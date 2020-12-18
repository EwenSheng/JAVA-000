package io.kimmking.rpcfx.api;

public interface MyRpcfxResolver {

    <T> T resolve(Class clazz);
}
