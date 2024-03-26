package com.github.zzq0010.example;

import com.github.zzq0010.example.service.EchoService;
import com.github.zzq0010.rpc.config.RPCConfig;
import com.github.zzq0010.rpc.framework.ProxyFactory;
import com.github.zzq0010.rpc.api.ProxyMode;

/**
 * 
 * @author leeyazhou
 *
 */
public class ConsumerMain {

    public static void main(String[] args) {
        EchoService echoService = ProxyFactory.getObject(RPCConfig.proxyMode).getProxy(EchoService.class);
        for (int i = 0; i< 100; i ++) {
            System.out.println("结果：" + echoService.echo("test echo"));
            System.out.println("结果：" + echoService.hello("Simple RPC"));
        }
        System.out.println("over");
    }
}
