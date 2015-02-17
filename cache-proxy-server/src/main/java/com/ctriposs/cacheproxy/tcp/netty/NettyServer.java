package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by tyaloo on 2015/2/17.
 */
public class NettyServer {
    static Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    public NettyServer() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new CacheChannelInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(9798).sync();
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void close() throws InterruptedException {
    }
}
