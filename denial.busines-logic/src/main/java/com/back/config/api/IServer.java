package com.back.config.api;

import java.util.function.Supplier;

/**
 * interface exposing a consuming server
 */
public interface IServer  {
    Supplier<IServer> LAZY = InstanceFactory.supplier(IServer.class);
    void  init(int port, Runnable r);
    void stop();

}
