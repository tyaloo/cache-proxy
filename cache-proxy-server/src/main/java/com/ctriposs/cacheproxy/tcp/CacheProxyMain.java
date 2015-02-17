package com.ctriposs.cacheproxy.tcp;


import java.util.logging.Logger;

/**
 * Created by tyaloo on 2015/2/17.
 */
public class CacheProxyMain {
    private static final Logger LOGGER = Logger.getLogger(CacheProxyMain.class.getName());

    public static void main(String[] args) {

        CacheProxy cacheProxy = null;
        try {
            cacheProxy = new CacheProxy();
            cacheProxy.start();
            System.out.println("start success");

        } catch (Exception e) {
            if(cacheProxy!=null)cacheProxy.close();
            LOGGER.info("Can not to start the CacheProxy then is going to shutdown");
        }
    }

}
