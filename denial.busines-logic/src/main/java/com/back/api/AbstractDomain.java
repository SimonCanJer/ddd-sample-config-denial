package com.back.api;

import java.io.Serializable;
import java.util.Map;
import java.util.function.*;

/**
 * The base abstrcat class of buisness logic, which defines
 * workflow
 * @see IPipeline
 */
public abstract class AbstractDomain  implements IDomain {
    @Override
    public IDataHolder.VarResult process(String clientID,String command) {
        return getDataPopulator().proceedRequest(clientID, getBusinessLogicPipeline(),getBusinessOperation(),getInitialTrailValue(),command);
    }
    protected abstract IDataHolder getDataPopulator();
    protected abstract  IPipeline getBusinessLogicPipeline();
    protected abstract BiConsumer<Map<String,Serializable>,String> getBusinessOperation();
    protected abstract Supplier<IDataHolder.CallTrailer> getInitialTrailValue();

}
