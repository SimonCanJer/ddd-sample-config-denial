package com.back.domain;

import com.back.api.AbstractDomain;
import com.back.api.IDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngineManager;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConcreteDomainOpenInject  extends AbstractDomain {
    Logger LOGGER = LoggerFactory.getLogger("SERVER");
    static final int MAX_CALL =5;
    static final long FRAME_LEN=5000L;
    @Override
    protected Function<IDataHolder.CallTrailer, Boolean> getBusinessLogic() {
        return this::processCall;
    }

    @Override
    protected Supplier<IDataHolder.CallTrailer> getInitialTrailValue() {
        return ()->{
            return new IDataHolder.CallTrailer();

        } ;
    }

    private boolean processCall(IDataHolder.CallTrailer trailer)
    {
        int count=1;

        if(System.currentTimeMillis()-trailer.getBirthTime()>FRAME_LEN)
        {
            trailer.setInitCounter(1);
        }
        else
            count=trailer.incrementAndGet();

        LOGGER.info(" count "+count+" when "+(System.currentTimeMillis()-trailer.getBirthTime())+" ellapsed");
        return count<=MAX_CALL;

    }
}
