package com.github.zzq0010.rpc.api;

import com.github.zzq0010.rpc.framework.ProxyFactory;
import com.github.zzq0010.rpc.proxy.CGLibProxyFactory;
import com.github.zzq0010.rpc.proxy.JDKProxyFactory;

public enum ProxyMode {

    JDK(JDKProxyFactory.class),
    CGLIB(CGLibProxyFactory.class);

    private Class<? extends ProxyFactory> proxyFactoryClazz;

    ProxyMode(Class<? extends ProxyFactory> proxyFactoryClazz) {
        this.proxyFactoryClazz = proxyFactoryClazz;
    }

    public Class<? extends ProxyFactory> getProxyFactoryClazz() {
        return proxyFactoryClazz;
    }
}
