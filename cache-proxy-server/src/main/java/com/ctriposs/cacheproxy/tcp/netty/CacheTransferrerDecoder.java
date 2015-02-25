package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * Created by tyaloo on 2015/2/17.
 */
public class CacheTransferrerDecoder extends ByteToMessageDecoder {

    private MemcacheProxy proxy = new MemcacheProxy();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        proxy.sendToStore(in,ctx);

    }

}
