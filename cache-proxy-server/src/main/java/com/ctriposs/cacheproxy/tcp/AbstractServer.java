package com.ctriposs.cacheproxy.tcp;


import java.util.logging.Logger;

/**
 * Created by jiang.j on 2015/2/17.
 */
public abstract class AbstractServer {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    protected String appName;

    public AbstractServer() throws Exception {
        init();
    }

    protected abstract void init() throws Exception;
    protected abstract void doStart() throws Exception;
    protected abstract void doClose() throws Exception;

    public void start() throws Exception {
    }

    public void close() {
    }

}
