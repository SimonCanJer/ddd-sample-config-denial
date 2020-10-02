package back;




import com.back.config.api.ConfigRegistry;
import com.back.config.api.IServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The class perform initialization of Server.
 *
 */
public class Server {
    static Logger LOGGER = LoggerFactory.getLogger("SERVER");
    private static final int[] PORT_RANGE =new int[]{8080,8099} ;
    static int findPort()
    {
        for(int port=PORT_RANGE[0];port<=PORT_RANGE[1];port++) {
            try {
                ServerSocket server = new ServerSocket(port);
                server.close();
                LOGGER.debug("found a free port to bind server "+port);
                return port;

            } catch (Exception e) {

            }
        }
        return -1;

    }

    static public void main(String[] args)
    {
        ConfigRegistry.initWithClassPath(Server.class,"application.properties").setup();
        int port=findPort();
        if(port<0)
            throw new RuntimeException("cannot find a free port in range");
        Thread t= new Thread(()->{
            try {
                IServer.LAZY.get().init(port, () -> {
                    System.out.println(" SERVER SIDE : SERVER STARTED ON PORT " + port + ",\n PRESS ENTER TO STOP");
                });

            }
              catch(Throwable th)
                {
                    th.printStackTrace();
                    throw new Error(th);
                }
        });
        t.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IServer.LAZY.get().stop();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
