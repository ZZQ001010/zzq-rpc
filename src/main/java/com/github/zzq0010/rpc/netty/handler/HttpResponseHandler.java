package com.github.zzq0010.rpc.netty.handler;

import com.github.zzq0010.rpc.Message;
import com.github.zzq0010.rpc.netty.HttpRPCClient;
import com.github.zzq0010.rpc.netty.HttpRPCClient;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

import java.nio.charset.StandardCharsets;

public class HttpResponseHandler extends ChannelInboundHandlerAdapter {

    private final static Gson GS  =new Gson();

    private HttpRPCClient httpClient;

    public HttpResponseHandler(HttpRPCClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse)
        {
            HttpResponse response = (HttpResponse) msg;
//            System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
        }
        if(msg instanceof HttpContent)
        {
            HttpContent httpContent = (HttpContent)msg;
            ByteBuf content = httpContent.content();
            CharSequence charSequence = content.readCharSequence(content.readableBytes(), StandardCharsets.UTF_8);
            String data = charSequence.toString();
            System.out.println("接受消息: " + data);
            Message message = GS.fromJson(data, Message.class);
            httpClient.res = message;
            content.release();
        }
    }
}