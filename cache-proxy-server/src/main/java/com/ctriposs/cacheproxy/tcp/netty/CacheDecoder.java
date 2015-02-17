package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by jiang.j on 2015/2/17.
 */
@ChannelHandler.Sharable
public class CacheDecoder extends MessageToMessageDecoder<ByteBuffer> {

    public CacheDecoder() {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuffer msg, List<Object> out) throws Exception {
        out.add(msg.array());
    }
}