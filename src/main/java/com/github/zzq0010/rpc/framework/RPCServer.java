package com.github.zzq0010.rpc.framework;

public interface RPCServer {

    void startup();

    public void registerProssor(String name, Object prossor);
}
