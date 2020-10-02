package com.back.api;

import com.back.config.api.InstanceFactory;

import java.util.function.Supplier;

/**
 * The interface exposed domain logic, which porcesses inoming request
 *
 */
public interface IDomain {
    Supplier<IDomain> LAZY = InstanceFactory.supplier(IDomain.class);
    /**
     * processes incoming request return false, when service is unvailablle for the client
     * @param clientID
     * @return
     */
     boolean process(int clientID);
}
