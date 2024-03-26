package com.github.zzq0010.rpc.framework;


import com.github.zzq0010.rpc.api.ProxyMode;

import java.lang.reflect.Constructor;

public interface ProxyFactory {

    <T> T getProxy(Class<T> interfaceClass);


    static ProxyFactory getObject(ProxyMode mode) {
        Class<? extends ProxyFactory> proxyFactoryClazz = mode.getProxyFactoryClazz();
        Constructor<? extends ProxyFactory> constructor = null;
        try {
            constructor = proxyFactoryClazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ProxyFactory proxyFactory = null;
        try {
            proxyFactory = constructor.newInstance();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return proxyFactory;
    }

}
