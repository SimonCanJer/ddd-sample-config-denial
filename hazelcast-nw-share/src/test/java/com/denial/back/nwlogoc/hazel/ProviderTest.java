package com.denial.back.nwlogoc.hazel;

import com.back.api.IDataHolder;
import com.back.api.IPipeline;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class ProviderTest {
    @Test
    public void test()
    {
        HazelcastCallTrailShare p=null;
        try {

             p = (HazelcastCallTrailShare) new HazelcastCallTrailShare().init();
        }
        catch(Throwable t)
        {
            assertNull(" init should be without an error",t);

        }
        AtomicInteger val= new AtomicInteger();
        long l=System.currentTimeMillis();

        for(int j=0;j<10;j++) {
            int i=j%5;

            IDataHolder.VarResult res = p.proceedRequest("id_" +i, new IPipeline() {
                @Override
                public IDataHolder.VarResult apply(IDataHolder.CallTrailer personalData, BiConsumer<Map<String, Serializable>, String> op, String command) {
                    if(System.currentTimeMillis()-personalData.getBirthTime()>5000)
                        personalData.setInitCounter(0);
                    if(personalData.getAndIncrement()<5)
                    {
                            op.accept(personalData.getData(),command);
                            return new IDataHolder.VarResult(personalData);

                    }
                    return new IDataHolder.VarResult();


                }
            }, new BiConsumer<Map<String, Serializable>, String>() {
                @Override
                public void accept(Map<String, Serializable> params, String s) {
                    params.put("command",s);
                    Integer ic= (Integer) params.computeIfAbsent("count",(k)->{return 0;});
                    params.put("count",++ic);
                    val.set((Integer) params.get("count"));

                }
            },()->{
                return new IDataHolder.CallTrailer();
            },"command");

            assertTrue(res.success());
            assertNull(res.getError());
            int expected=(j<5)?1:2;
            assertEquals(expected,res.getNextValue().get("count"));
        }
        assertEquals(5,p.registeredClients());

    }

}