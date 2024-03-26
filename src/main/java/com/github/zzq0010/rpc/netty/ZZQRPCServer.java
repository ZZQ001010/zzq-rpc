/**
 * 
 */
package com.github.zzq0010.rpc.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.zzq0010.rpc.ProtocolCodec;
import com.github.zzq0010.rpc.framework.RPCServer;
import com.github.zzq0010.rpc.netty.handler.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author leeyazhou
 *
 */
public class ZZQRPCServer implements RPCServer {
    private static final Logger                    logger       = LoggerFactory.getLogger(ZZQRPCServer.class);
    private final String                           host;
    private final int                              port;
    private volatile ConcurrentMap<String, Object> prossorCache = new ConcurrentHashMap<>();

    public ZZQRPCServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startup() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup ioWorker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, ioWorker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolCodec());
                ch.pipeline().addLast("handler", new ServerHandler(prossorCache));
            }
        });

        try {
            bootstrap.bind(host, port).sync().addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("NettyServer start success! port : {}", this.port);
                } else {
                    logger.error("", future.cause());
                }
            });
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    public void registerProssor(String name, Object prossor) {
        this.prossorCache.put(name, prossor);
    }
}
