package com.denial.back.jersey;

import com.back.config.api.IServer;
import com.denial.back.config.Config;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * The class manages build and run of Jersey framework server on the top of
 * Jetty. It builds Jersey server over glassfish fw and performs, undercarpet DI using
 * glashfish DI (Guice like)
 * @see Config
 * for implementation
 * @see JerseyServer registered in the config
 */
public  class JerseyServer implements IServer {

    static public void main(String[] server)
    {
        new JerseyServer().init(8083,()->{});
        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    Runnable destroyer;
    Logger LOGGER= Logger.getLogger(this.getClass());
    @Override
    public void init(int  port,Runnable onStart) {
       LOGGER.info("Starting Jetty Server in the port "+port);
        Runnable onStarted=null;

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        //Sparter is config
        context.addServlet(new ServletHolder(new ServletContainer(new Config())), "/*");
        Server jettyServer = new Server(port);
        jettyServer.setHandler(context);
        destroyer= ()->{
            try {
                jettyServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

            jettyServer.destroy();

        };
        try {
            jettyServer.start();
            try {
                if (onStart != null)
                    onStart.run();
            }
            catch(Throwable t)
            {

            }

            jettyServer.join();
        }
        catch(Exception e)
        {

        }

    }

    @Override
    public void stop() {
        if(destroyer!=null)
            destroyer.run();
    }


}
