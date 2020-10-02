package com.back.config.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigRegistry {
    static public IConfig initWithClassPath(Class anchor, String propsFile)
    {

        if(anchor==null)
        {
            anchor = ConfigRegistry.class;
            if(propsFile==null)
            {
                propsFile="application.properties";
            }
        }
        Class ancF=anchor;
        String strF=propsFile;
        return new IConfig() {
            Properties props= readPropertiesFile(ancF,strF);
            {
                if(props==null)
                {
                    props=new Properties();
                }
            }
            @Override
            public IConfig withServerSupplier(String name) {
                props.setProperty(SERVER_CLASS_PROPERTY,name);
                return this;
            }

            @Override
            public IConfig withDomainSupplier(String name) {
                props.setProperty(SERVER_CLASS_PROPERTY,name);
                return this;
            }

            @Override
            public void setup() {
                if(props.getProperty(SERVER_CLASS_PROPERTY)==null||props.getProperty(DOMAIN_CLASS_PROPERTY)==null)
                {
                    throw new UnsatisfiedLinkError("do not now how to bind suppliers");
                }
                InstanceFactory.setup(props);

            }
        };

    }
       static Properties readPropertiesFile(Class anchor,String fileName)  {
        InputStream is = null;
        Properties prop = null;
        try {
            is =  anchor.getClassLoader().getResourceAsStream("application.properties");
            prop = new Properties();
            prop.load(is);
        } catch(Exception enf) {

            throw new Error(enf);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }
}
