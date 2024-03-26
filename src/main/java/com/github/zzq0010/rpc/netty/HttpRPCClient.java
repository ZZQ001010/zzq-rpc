package com.github.zzq0010.rpc.netty;

import com.github.zzq0010.rpc.Invocation;
import com.github.zzq0010.rpc.Message;
import com.github.zzq0010.rpc.framework.RPCClient;
import com.github.zzq0010.rpc.netty.handler.HttpResponseHandler;
import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpRPCClient implements RPCClient {

    public volatile Message res;

    private static final AtomicInteger idCreater   = new AtomicInteger();

    private final String                                   host;
    private final int                                      port;

    private Bootstrap bootstrap = new Bootstrap();

    public HttpRPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public void init() {

    }

    public void startup() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch){
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpResponseHandler(HttpRPCClient.this));
                }
            });
    }

    public Object invoke(Invocation invocation) {
        ChannelFuture cf = bootstrap.connect(host, port);
        Channel channel = cf.channel();
        Message msg = new Message().setInvocation(invocation);
        msg.id(idCreater.incrementAndGet());
        String json = new Gson().toJson(msg);
        System.out.println("发送消息: " + json);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(json.getBytes());
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/",
                byteBuf);
        request.headers().set(HttpHeaders.Names.HOST, host);
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        channel.writeAndFlush(request);
       while (Objects.isNull(res)) {
           System.out.println("------------等待响应");
       }
       return res.getResponse();
    }


}