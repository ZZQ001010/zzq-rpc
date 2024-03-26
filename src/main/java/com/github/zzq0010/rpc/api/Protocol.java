package com.github.zzq0010.rpc.api;

import com.github.zzq0010.rpc.framework.RPCClient;
import com.github.zzq0010.rpc.framework.RPCServer;
import com.github.zzq0010.rpc.netty.*;

public enum Protocol {

    HTTP(HttpRPCClient.class, HttpRPCServer.class),
    ZZQ(ZZQRPCClient.class, ZZQRPCServer.class);

    private Class<? extends RPCClient> rpcClientClazz;

    private Class<? extends RPCServer> rpcServerClazz;

    Protocol(Class<? extends RPCClient> client, Class<? extends  RPCServer> server) {
        this.rpcClientClazz = client;
        this.rpcServerClazz = server;
    }

    public Class<? extends RPCClient> getRpcClientClazz() {
        return rpcClientClazz;
    }

    public Class<? extends RPCServer> getRpcServerClazz() {
        return rpcServerClazz;
    }

}
