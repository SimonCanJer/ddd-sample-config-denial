package com.denial.back.config;

import com.back.config.api.IServer;
import com.denial.back.spring.SpringServer;


import java.util.function.Supplier;

public class SpringSupplier  implements Supplier<IServer> {

    @Override
    public IServer get() {
        return new SpringServer();
    }
}
