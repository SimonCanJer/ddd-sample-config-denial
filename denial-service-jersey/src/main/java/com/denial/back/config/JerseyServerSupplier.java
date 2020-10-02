package com.denial.back.config;


import com.back.config.api.IServer;
import com.denial.back.jersey.JerseyServer;

import java.util.function.Supplier;

public class JerseyServerSupplier implements Supplier<IServer> {

    @Override
    public IServer get() {
        return new JerseyServer();
    }
}
