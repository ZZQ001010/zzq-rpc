/**
 * 
 */
package com.github.zzq0010.rpc.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.github.zzq0010.rpc.Invocation;
import com.github.zzq0010.rpc.URL;
import com.github.zzq0010.rpc.config.RPCConfig;
import com.github.zzq0010.rpc.framework.RPCClient;

/**
 * @author leeyazhou
 *
 */
public class ProxyHandler implements InvocationHandler {

    private Class<?>    interfaceClass;
    private URL         url;
    private RPCClient client;

    public ProxyHandler(Class<?> interfaceClass, RPCConfig config) {
        this.interfaceClass = interfaceClass;
        this.url = new URL().setHost(config.serviceHost).setPort(config.servicePort);
        Class<? extends RPCClient> rpcClientClazz = config.protocol.getRpcClientClazz();
        Constructor<? extends RPCClient> constructor = null;
        try {
            constructor = rpcClientClazz.getConstructor(String.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        RPCClient rpcClient = null;
        try {
            rpcClient = constructor.newInstance(config.serviceHost, config.servicePort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.client = rpcClient;
        this.client.init();
        this.client.startup();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("toString".equals(method.getName()) && method.getParameterCount() == 0) {
            return toString();
        }

        Invocation invocation = new Invocation().setServiceName(interfaceClass.getName());
        invocation.setServiceMethod(method.getName()).setArgs(args);
        if (args != null) {
            String[] argTypes = new String[method.getParameterTypes().length];
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                argTypes[i] = method.getParameterTypes()[i].getName();
            }
            invocation.setArgTypes(argTypes);
        }

        return this.client.invoke(invocation);
    }

}
