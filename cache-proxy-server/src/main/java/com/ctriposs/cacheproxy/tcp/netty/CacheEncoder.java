package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;
import java.util.List;


/**
 * Created by tyaloo on 2015/2/17.
 */
@ChannelHandler.Sharable
public class CacheEncoder extends MessageToMessageEncoder<byte[]> {

    public CacheEncoder() {

    }

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        if (msg.length == 0) {
            return;
        }

        out.add(Unpooled.copiedBuffer(msg));
    }
}