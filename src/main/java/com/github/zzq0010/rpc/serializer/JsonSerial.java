package com.github.zzq0010.rpc.serializer;

import com.github.zzq0010.rpc.framework.Serial;

public class JsonSerial implements Serial {
    @Override
    public <T> T decode(byte[] bytes) throws Exception {
        return null;
    }

    @Override
    public <T> byte[] encode(T bytes) throws Exception {
        return new byte[0];
    }
}
