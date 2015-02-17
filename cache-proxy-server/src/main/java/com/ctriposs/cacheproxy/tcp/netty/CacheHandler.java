package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by jiang.j on 2015/2/17.
 */
public class CacheHandler extends SimpleChannelInboundHandler<byte[]> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, byte[] request) throws Exception {
        MemcacheProxy.callMemcachServer(request,ctx);

    }
}
