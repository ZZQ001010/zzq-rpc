package com.github.zzq0010.rpc.serializer;

import com.github.zzq0010.rpc.framework.Serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class  JdkSerial  implements Serial {

    @Override
    public <T> T decode(byte[] bytes) throws Exception {
        ObjectInputStream objectIn = null;
        Object resultObject = null;
        try {
            objectIn = new ObjectInputStream(new ByteArrayInputStream(bytes));
            resultObject = objectIn.readObject();
        } finally {
            if (null != objectIn) {
                objectIn.close();
            }
        }

        return (T) resultObject;
    }

    @Override
    public <T> byte[] encode(T bytes) throws Exception {
        ByteArrayOutputStream byteArray = null;
        ObjectOutputStream output = null;
        try {
            byteArray = new ByteArrayOutputStream();
            output = new ObjectOutputStream(byteArray);
            output.writeObject(bytes);
            output.flush();
        } finally {
            if (null != output) {
                output.close();
            }
        }
        return byteArray.toByteArray();
    }

}