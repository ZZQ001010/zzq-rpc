/**
 * 
 */
package com.github.zzq0010.rpc.proxy;

import java.lang.reflect.Proxy;

import com.github.zzq0010.rpc.config.RPCConfig;
import com.github.zzq0010.rpc.framework.ProxyFactory;

/**
 * @author zzq
 */
public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> interfaceClass) {
        RPCConfig clientConfig = new RPCConfig();
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { interfaceClass },
                new ProxyHandler(interfaceClass, clientConfig));
    }

}
