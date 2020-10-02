package com.back.config.api;


import com.back.api.IDomain;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * The class used in order not to involve a heavy  DI frameworks as Spring or Guice.
 * It provides a lazy building of required implementers
 *
 */
public class InstanceFactory {

    private static class InterfaceFactory<I>
    {
        Supplier<? extends I> implementer;
        AtomicReference<I> refInstance=new AtomicReference<>();
        I create() throws Exception {
            if(refInstance.get()==null) {
                synchronized (refInstance) {
                    refInstance.set(implementer.get());
                }
            }
            return refInstance.get();
        }
        Supplier<I> p= ()-> {
            try {
                return create();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error(e);
            }

        };
    }

    private static final Map<Class<?>,InterfaceFactory> mapProducers= new HashMap<>();
    static {
        mapProducers.put(IDomain.class, new InterfaceFactory<IDomain>());
        mapProducers.put(IServer.class, new InterfaceFactory<IServer>());
    }


    private static <I> Supplier<I> createInstance(Class<I> cl)
    {
        try {
            return (Supplier<I>) cl.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }

    }
    static void setup(Properties props)
    {
      //  Properties props= readPropertiesFile();

        String server= props.getProperty("server.class");
        String domain = props.getProperty("domain.class");
        ClassLoader loader= InstanceFactory.class.getClassLoader();
        try
        {
            mapProducers.get(IDomain.class).implementer=createInstance(loader.loadClass(domain));
            mapProducers.get(IServer.class).implementer= createInstance(loader.loadClass(server));

        }
        catch(Exception e)
        {
            throw new Error(e);
        }
    }
    public static <I> Supplier<I> supplier(Class<I> clazz)
    {
        if(!mapProducers.containsKey(clazz))
        {
            throw new Error("unsupported interface "+clazz);
        }
        return  mapProducers.get(clazz).p;
    }


}
