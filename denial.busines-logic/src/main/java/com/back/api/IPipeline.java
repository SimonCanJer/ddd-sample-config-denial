package com.back.api;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The functional interface defines call for business logic pipeline
 * Remark. For a more classsic case of DDD, BiConsumer should be extended by an interface
 * with domain specific name.
 */
@FunctionalInterface
public interface IPipeline {
    IDataHolder.VarResult apply(IDataHolder.CallTrailer personalData, BiConsumer<Map<String, Serializable>,String> op,String command);
}
