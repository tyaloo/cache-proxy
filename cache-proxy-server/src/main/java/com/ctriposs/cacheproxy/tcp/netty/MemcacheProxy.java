package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by tyaloo on 2015/2/16.
 */
public class MemcacheProxy{
    private static SockIOPool pool = null;
    private static final String VALUE = "VALUE";            // start of value line from server
    private static final String STATS        = "STAT";			// start of stats line from server
    private static final String END          = "END";			// end of data from server
    static {
         if (pool == null) {
            String[] serverlist = {
                    "192.168.136.128:11211"
            };

            Integer[] weights = {1, 1, 1, 1, 10, 5, 1, 1, 1, 3};

            // initialize the pool for memcache servers
            pool = SockIOPool.getInstance("test");
            pool.setServers(serverlist);
            pool.setWeights(weights);
            pool.setMaxConn(250);
            pool.setNagle(false);
            pool.setHashingAlg(SockIOPool.CONSISTENT_HASH);
            pool.initialize();
        }
    }

    private int needReadCount = 0;
    SockIOPool.SockIO currentSock=null;
    public  void sendToStore(ByteBuf in,ChannelHandlerContext ctx) throws IOException {

        int readableCount = in.readableBytes();
        if(needReadCount>0) {
            if (readableCount >= needReadCount) {
                currentSock.write(in.readBytes(needReadCount).array());
                needReadCount = 0;
            } else {
                currentSock.write(in.readBytes(readableCount).array());
                needReadCount -= readableCount;
            }
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

            if(currentSock == null)
                currentSock = pool.getSock("test", 12345);

            currentSock.write(frame.array());

            readableCount = in.readableBytes();

            if(readableCount>=needReadCount){
                currentSock.write(in.readBytes(needReadCount).array());
                needReadCount=0;
            }
            else{
                currentSock.write(in.readBytes(readableCount).array());
                needReadCount-=readableCount;
            }
            System.out.println(frame.toString(Charset.defaultCharset()));
        }

        if (needReadCount == 0 && currentSock!=null) {
            currentSock.flush();
            setResponseBytes(currentSock,ctx);
            currentSock.close();
            currentSock=null;
            ctx.flush();
        }
    }

    private static void setResponseBytes(SockIOPool.SockIO sock,ChannelHandlerContext ctx) throws IOException {
        String line = sock.readLine();

        ctx.write(Unpooled.copiedBuffer(line + "\r\n", Charset.defaultCharset()));
        if(line.startsWith(VALUE)) {
            boolean isEnd =false;
            while (!isEnd) {

                String[] info = line.split(" ");
                System.out.println(line);
                int length = Integer.parseInt(info[3])+2;
                int endP =0;
                while (endP<length) {
                    int readCount = 1024;
                    if(length-endP<readCount){
                        readCount = length-endP;
                    }

                   endP+=readCount;

                    byte[] buffer= new byte[readCount];
                    sock.read(buffer);
                    ctx.write(Unpooled.copiedBuffer(buffer));
                }

                line = sock.readLine();
                if(line.startsWith(END)){
                   isEnd=true;
                }
                ctx.write(Unpooled.copiedBuffer(line + "\r\n", Charset.defaultCharset()));
            }
        }
        else if(line.startsWith(STATS)){
            boolean isEnd =false;
            while (!isEnd) {

                line = sock.readLine();
                if(line.startsWith(END)){
                    isEnd=true;
                }
                ctx.write(Unpooled.copiedBuffer(line + "\r\n", Charset.defaultCharset()));
            }

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
