/**
 * 
 */
package com.github.zzq0010.example;

import com.github.zzq0010.example.service.EchoService;
import com.github.zzq0010.example.service.EchoServiceImpl;
import com.github.zzq0010.rpc.framework.RPCServer;
import com.github.zzq0010.rpc.config.RPCConfig;

import java.lang.reflect.Constructor;

/**
 * @author leeyazhou
 *
 */
public class ProviderMain {

    public static void main(String[] args) {
        RPCConfig config = new RPCConfig();
        Class<? extends RPCServer> rpcServerClazz = config.protocol.getRpcServerClazz();
        Constructor<? extends RPCServer> constructor = null;
        try {
            constructor = rpcServerClazz.getConstructor(String.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        RPCServer rpcServer = null;
        try {
            rpcServer = constructor.newInstance(config.serviceHost, config.servicePort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcServer.registerProssor(EchoService.class.getName(), new EchoServiceImpl());
        rpcServer.startup();
    }
}
