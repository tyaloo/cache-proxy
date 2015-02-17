package com.ctriposs.cacheproxy.tcp.netty;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

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

   public static void callMemcachServer(byte[] request,ChannelHandlerContext ctx) throws IOException {
        SockIOPool.SockIO sock=null;
       try {
           sock = pool.getSock("test", 12345);
           sock.write(request);
           sock.flush();
           setResponseBytes(sock, ctx);
       }finally {
           if(sock!=null&&sock.isAlive())
              sock.close();
       }

   }

    private static void setResponseBytes(SockIOPool.SockIO sock,ChannelHandlerContext ctx) throws IOException {
        String line = sock.readLine();

        ctx.write((line + "\r\n").getBytes());
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
                    ctx.write(buffer);
                }

                line = sock.readLine();
                if(line.startsWith(END)){
                   isEnd=true;
                }
                ctx.write((line + "\r\n").getBytes());
            }
        }
        else if(line.startsWith(STATS)){
            boolean isEnd =false;
            while (!isEnd) {

                line = sock.readLine();
                if(line.startsWith(END)){
                    isEnd=true;
                }
                ctx.write((line + "\r\n").getBytes());
            }

        }






    }
    private static byte[] getResponseBytes(SockIOPool.SockIO sock) throws IOException {
        String line = sock.readLine();

        System.out.println(line);
        byte[] valueBytes = null;
        while(line.startsWith(VALUE)) {
            String[] info = line.split(" ");
//            int flag = Integer.parseInt(info[2]);
            int length = Integer.parseInt(info[3]);

            // read obj into buffer
            valueBytes = new byte[length];
            sock.read(valueBytes);
            sock.clearEOL();
            sock.readLine();

        }

        byte[] cmdBytes = (line +"\r\n").getBytes();

        if(valueBytes!=null){

            byte[] rtn = new byte[cmdBytes.length+valueBytes.length+7];

            System.arraycopy(cmdBytes,0,rtn,0,cmdBytes.length);
            System.arraycopy(valueBytes,0,rtn,cmdBytes.length,valueBytes.length);
            rtn[rtn.length-7]='\r';
            rtn[rtn.length-6]='\n';
            rtn[rtn.length-5]='E';
            rtn[rtn.length-4]='N';
            rtn[rtn.length-3]='D';
            rtn[rtn.length-2]='\r';
            rtn[rtn.length-1]='\n';

            return rtn;
        }else{

            return  cmdBytes;
        }

    }
}
