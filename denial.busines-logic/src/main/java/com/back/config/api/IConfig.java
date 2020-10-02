package com.back.config.api;

public interface IConfig {
    static final String SERVER_CLASS_PROPERTY="server.class";
    static final String DOMAIN_CLASS_PROPERTY="domain.class";

    IConfig withServerSupplier(String name);
    IConfig withDomainSupplier(String name);
    void    setup();
}
