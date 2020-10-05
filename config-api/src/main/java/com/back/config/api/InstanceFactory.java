package com.back.config.api;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * The class initializes instances of concrete servers.
 * @see #setup(Map, Properties)
 * It is called on the bottom line of configuration
 * @see ConfigRegistry
 * @see IConfig
 * @see, IPropertyBinder
 * Besides,the class used in order not to involve a heavy  DI frameworks as Spring or Guice.
 * It provides a lazy building of required implementers, and provides suppliers of instances by interface.
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



    private static <I> Supplier<I> createInstance(Class<I> cl)
    {
        try {
            return (Supplier<I>) cl.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }

    }
    static void setup(Map<Class, String> map, Properties props)
    {
      //  Properties props= readPropertiesFile();
        ClassLoader loader= InstanceFactory.class.getClassLoader();
        //String server= props.getProperty("server.class");

        //String domain = props.getProperty("domain.class");
        map.entrySet().stream().forEach((entry)->{
            try
            {
                InterfaceFactory ifact= new InterfaceFactory();
                ifact.implementer=createInstance(loader.loadClass(props.getProperty(entry.getValue())));
                mapProducers.put(entry.getKey(),ifact);



            }
            catch(Exception e)
            {
                throw new Error(e);
            }


        });


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
