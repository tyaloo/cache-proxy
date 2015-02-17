package com.ctriposs.cacheproxy.tcp;

import com.ctriposs.cacheproxy.tcp.netty.NettyServer;

import java.util.logging.Logger;

/**
 * Created by tyaloo on 2015/2/17.
 */
public class CacheProxy extends AbstractServer{
    static Logger LOG = Logger.getLogger(CacheProxy.class.getName());

    private NettyServer nettyServer;

    public CacheProxy() throws Exception {}

    @Override
    protected void init() throws Exception {
        initGate();
        nettyServer = new NettyServer();
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doClose() throws InterruptedException {
        nettyServer.close();
    }

    private void initGate() throws Exception {
        LOG.info("Starting Groovy Filter file manager");


        LOG.info("Groovy Filter file manager started");
    }
}
