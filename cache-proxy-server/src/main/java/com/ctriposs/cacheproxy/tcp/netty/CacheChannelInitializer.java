package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by tyaloo on 2015/2/17.
 */
public class CacheChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline()
                .addLast(new CacheTransferrerDecoder());
    }
}
