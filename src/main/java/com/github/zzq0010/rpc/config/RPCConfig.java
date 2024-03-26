package com.github.zzq0010.rpc.config;

import com.github.zzq0010.rpc.api.Protocol;
import com.github.zzq0010.rpc.api.ProxyMode;
import com.github.zzq0010.rpc.framework.Serial;
import com.github.zzq0010.rpc.serializer.JdkSerial;

public class RPCConfig {

    public Protocol protocol = Protocol.HTTP;

    public Serial serial =  new JdkSerial();

    public static ProxyMode proxyMode = ProxyMode.CGLIB;

    public int servicePort = 8080;

    public String serviceHost = "127.0.0.1";
}
