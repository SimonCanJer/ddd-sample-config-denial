package com.back.domain;

import com.back.api.AbstractDomain;
import com.back.api.IDataHolder;
import com.back.api.IPipeline;
import javax.script.ScriptEngineManager;
import java.io.Serializable;
import java.util.Map;
import java.util.function.*;
import java.util.logging.Logger;

/**
 * Closed /concrete implementation of business logc, but open for injection.
 * Basically I could keep here getters/setters of IDataHolder, but seems it could force
 * users to break common system design, whem @Autowired, of @Inject used.
 *
 */

public class ConcreteDomainObject extends AbstractDomain {
    Logger LOGGER = java.util.logging.Logger.getLogger("SERVER");
    static final int MAX_CALL =5;
    static final long FRAME_LEN=5000L;

    /**
     * concrete delivery of processing pipeline
     * @return: implementation of the IPipeline interface
     */
    @Override
    protected IPipeline getBusinessLogicPipeline() {
        return this::processCall;
    }

    /**
     * Intializes data when it is absent in a storage
     * @return
     */
    @Override
    protected Supplier<IDataHolder.CallTrailer> getInitialTrailValue() {
        return ()->{
            return new IDataHolder.CallTrailer();

        } ;
    }

    /**
     * concrete implementation of the  working pipeline
     * @param trailer    data trailer
     * @param op         business operfation
     * @param command    command text
     * @return           processing result
     */
    private IDataHolder.VarResult processCall(IDataHolder.CallTrailer trailer, BiConsumer<Map<String,Serializable>,String> op,String command)
    {
        int count=1;

        if(System.currentTimeMillis()-trailer.getBirthTime()>FRAME_LEN)
        {
            trailer.setInitCounter(1);
        }
        else
            count=trailer.incrementAndGet();

        LOGGER.info(" count "+count+" when "+(System.currentTimeMillis()-trailer.getBirthTime())+" ellapsed");
        boolean legal=count<=MAX_CALL;
        if(!legal)
            return new IDataHolder.VarResult();
        if(op!=null) {
            try {
                op.accept(trailer.getData(), command);
            }
            catch(Throwable t)
            {
               StringBuilder sb= new StringBuilder();
               sb.append(sb.append(t.getMessage()));
               String src=null;
               if(t.getCause()!=null)
               {
                   src = t.getCause().getMessage();
               }
               if(src!=null)
               {
                   sb.append(" cause "+src);
               }
                return new IDataHolder.VarResult(sb.toString());
            }
        }

        return new IDataHolder.VarResult(trailer);


    }
    ScriptProcessor processor= new ScriptProcessor();

    /**
     * returns concrete business oper\ation
     * @return consumer which performs the operation and yes,modifies the property map
     */
    protected BiConsumer<Map<String,Serializable>,String> getBusinessOperation()
    {
        return new BiConsumer<Map<String, Serializable>, String>() {
            @Override
            public void accept(Map<String, Serializable> data, String s) {
                try
                {
                    Serializable res=processor.withScriptProcessor("groovy").eval(s,data);
                    if(res!=null)
                        data.put("last_result",res);

                }
                catch(Exception e)
                {
                    throw new Error(e);
                }

            }
        };
    }
}
