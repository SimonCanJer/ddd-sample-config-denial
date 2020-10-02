package com.back.api;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractDomain  implements IDomain{
    @Override
    public boolean process(int clientID) {
        return getDataPopulator().proceedRequest(clientID,getBusinessLogic(),getInitialTrailValue());
    }
    protected abstract IDataHolder getDataPopulator();
    protected abstract Function<IDataHolder.CallTrailer, Boolean> getBusinessLogic();
    protected abstract Supplier<IDataHolder.CallTrailer> getInitialTrailValue();

}
