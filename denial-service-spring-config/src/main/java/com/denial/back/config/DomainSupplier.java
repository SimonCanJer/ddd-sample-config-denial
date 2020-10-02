package com.denial.back.config;

import com.back.api.IDomain;
import com.denial.back.spring.SpringConfig;

import java.util.function.Supplier;

public class DomainSupplier implements Supplier<IDomain> {
    @Override
    public IDomain get() {
        return SpringConfig.getContext().getBean(IDomain.class);
    }
}
