package com.github.zzq0010.rpc.netty.handler;

import com.github.zzq0010.rpc.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;


public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger                    logger       = LoggerFactory.getLogger(HttpRequestHandler.class);

    private volatile ConcurrentMap<String, Object> prossorCache = new ConcurrentHashMap<>();

    private Gson                                   GS           = new Gson();


    public HttpRequestHandler(ConcurrentMap<String, Object> prossorCache) {
        this.prossorCache = prossorCache;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
         ctx.flush();
    }


//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
//        String url = request.uri();
//        ByteBuf content = request.content();
//        CharSequence charSequence = content.readCharSequence(content.readableBytes(), StandardCharsets.UTF_8);
//        String data = charSequence.toString();
//
//        String[] split = url.split("/");
//        String processorName = split[0];
//        String methodName = split[1];
//        Type type = new TypeToken<Map<String, List<Object>>>(){}.getType();
//        Map<String, List<Object>> dataMap = GS.fromJson(data, type);
//        List<Object> types = dataMap.get("types");
//        List<Object> args1 = dataMap.get("args");
//        Class[] clazz = new Class[types.size()];
//        Object[] args = new Object[types.size()];
//        for (int i = 0; i < types.size(); i++) {
//            clazz[i] = Class.forName(types.get(i).toString());
//            args[i] = GS.fromJson()
//        }
//        Object o = prossorCache.get(processorName);
//        Method method = o.getClass().getMethod(methodName, clazz);
//
//        method.invoke(o, )
//    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        ByteBuf content = request.content();
        CharSequence charSequence = content.readCharSequence(content.readableBytes(), StandardCharsets.UTF_8);
        String data = charSequence.toString();
        Message message = GS.fromJson(data, Message.class);
        Object o = doProcess(message);
        message.setResponse(o);
        FullHttpResponse fullHttpResponse = responseOk(HttpResponseStatus.OK, copiedBuffer(GS.toJson(message), CharsetUtil.UTF_8));
        ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse responseOk(HttpResponseStatus status, ByteBuf buf) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status,buf);
        if(buf != null){
            response.headers().set("Content-Type","application/json;charset=UTF-8");
            response.headers().set("Content-Length",response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        return response;
    }

    private ByteBuf copiedBuffer(String data, Charset utf8) {
        return Unpooled.wrappedBuffer(data.getBytes());
    }
    public Object doProcess(Message msg) throws Exception {
        Object prossor = prossorCache.get(msg.getInvocation().getServiceName());
        if (prossor == null) {
            throw new IllegalAccessError("no prossor [" + msg.getInvocation().getServiceName() + "] found");
        }

        Class<?>[] argTypes = new Class<?>[msg.getInvocation().getArgTypes().length];
        for (int i = 0; i < msg.getInvocation().getArgTypes().length; i++) {
            argTypes[i] = Class.forName(msg.getInvocation().getArgTypes()[i]);
        }

        Method method = prossor.getClass().getMethod(msg.getInvocation().getServiceMethod(), argTypes);
        Object result = method.invoke(prossor, msg.getInvocation().getArgs());
        logger.info("服务器端处理结果：{}", result);
        return result;
    }
}
