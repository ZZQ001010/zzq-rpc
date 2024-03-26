/**
 * 
 */
package com.github.zzq0010.rpc.netty.handler;

import com.github.zzq0010.rpc.Message;
import com.github.zzq0010.rpc.netty.ZZQRPCClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author leeyazhou
 *
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private ZZQRPCClient client;

    public ClientHandler(ZZQRPCClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        client.response(msg);
        logger.info("客户端收到响应" + msg);
    }

}
