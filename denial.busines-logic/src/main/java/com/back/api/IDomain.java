package com.back.api;

import com.back.config.api.InstanceFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The interface exposed domain logic, which processes incoming request.
 * Has built in injection mechanism
 *
 */
public interface IDomain {
    Supplier<IDomain> LAZY = InstanceFactory.supplier(IDomain.class);
    class OutOfQuteException extends RuntimeException
    {
        OutOfQuteException(String message)
        {
            super(message);
        }
    }
    /**
     * processes incoming request return false, when service is unvailablle for the client
     * @param clientID
     * @return
     */
     IDataHolder.VarResult process(String clientID,String command);
}
