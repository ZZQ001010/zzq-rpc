package com.github.zzq0010.rpc.proxy;

import com.github.zzq0010.rpc.config.RPCConfig;
import com.github.zzq0010.rpc.framework.ProxyFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CGLibProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Class<T> interfaceClass) {
        ProxyHandler proxyHandler = new ProxyHandler(interfaceClass, new RPCConfig());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) -> {
            Object res = proxyHandler.invoke(methodProxy, method, args);
            return res;
        });
        return (T) enhancer.create();
    }
}
