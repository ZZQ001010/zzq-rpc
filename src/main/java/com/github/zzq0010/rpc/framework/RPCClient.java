package com.github.zzq0010.rpc.framework;

import com.github.zzq0010.rpc.Invocation;

public interface RPCClient {

    void init();


    void startup();


    Object invoke(Invocation invocation);
}
