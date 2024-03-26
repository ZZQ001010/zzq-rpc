package com.github.zzq0010.rpc.framework;

public interface Serial {

    <T> T decode(byte[] bytes) throws Exception;


    <T> byte[] encode(T bytes) throws Exception;


}
