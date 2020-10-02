package com.denial.back.spring;


import com.back.config.api.IServer;

public class SpringServer implements IServer {

    @Override
    public void init(int port, Runnable r) {
        System.getProperties().setProperty("server.port",String.valueOf(port));
        if(r!=null)
            r.run();
        SpringConfig.start();
    }

    @Override
    public void stop() {
        System.exit(1);

    }
}
