package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by tyaloo on 2015/2/17.
 */
public class CacheTransferrerDecoder extends ByteToMessageDecoder {

    private int needReadCount = 0;
    private ByteBuffer cmdBuffer;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        while (needReadCount > 0 && in.isReadable()) {
            cmdBuffer.put(in.readByte());
            needReadCount--;
        }

        final int eol = findEndOfLine(in);
        final int length = eol - in.readerIndex();

        if (eol > 0) {
            final int delimLength = in.getByte(eol) == '\r' ? 2 : 1;
            final ByteBuf frame = in.readBytes(length + delimLength);
            String cmd = frame.toString(Charset.defaultCharset());

            String[] tmp = cmd.split("\\s");
            if (tmp.length > 3) {
                needReadCount = Integer.parseInt(tmp[tmp.length - 1]) + 2;
            }

            cmdBuffer = ByteBuffer.allocate(length + delimLength + needReadCount);

            cmdBuffer.put(frame.array());
            while (in.isReadable() && needReadCount > 0) {
                //currentSock.write(in.readByte());
                cmdBuffer.put(in.readByte());
                needReadCount--;
            }
            System.out.println(frame.toString(Charset.defaultCharset()));
        }

        if (needReadCount == 0 && cmdBuffer != null) {
            out.add(cmdBuffer);
            cmdBuffer.clear();
            cmdBuffer=null;
        }

    }

    private static int findEndOfLine(final ByteBuf buffer) {
        final int n = buffer.writerIndex();
        for (int i = buffer.readerIndex(); i < n; i++) {
            final byte b = buffer.getByte(i);
            if (b == '\n') {
                return i;
            } else if (b == '\r' && i < n - 1 && buffer.getByte(i + 1) == '\n') {
                return i;  // \r\n
            }
        }
        return -1;  // Not found.
    }
}
