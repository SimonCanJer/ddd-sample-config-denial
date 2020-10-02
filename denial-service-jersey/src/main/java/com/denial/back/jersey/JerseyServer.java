package com.denial.back.jersey;

import com.back.config.api.IServer;
import com.denial.back.config.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * The class manages build and run of Jersey framework server on the top of
 * Jetty
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
    @Override
    public void init(int  port,Runnable onStart) {
        System.out.println("Starting Jetty Server in the port "+port);
        Runnable onStarted=null;

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

       /* ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",Config.class.getCanonicalName()
                /*JerseyController.class.getCanonicalName());*/
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
