package com.denial.back.spring;


import com.back.config.api.IServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringServer implements IServer {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(int port, Runnable r) {
        System.getProperties().setProperty("server.port",String.valueOf(port));
        try {
            SpringConfig.start();
        }
        catch(Throwable t)
        {
            LOGGER.info("Failed to start server");
        }
        if(r!=null)
            r.run();

    }

    @Override
    public void stop() {
        System.exit(1);

    }
}
