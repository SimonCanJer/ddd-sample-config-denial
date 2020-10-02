package com.denial.back.nwlogoc.hazel;

import com.back.api.IDataHolder;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

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
        for(int i=0;i<=10;i++) {
            boolean res = p.proceedRequest(100, new Function<IDataHolder.CallTrailer, Boolean>() {
                @Override
                public Boolean apply(IDataHolder.CallTrailer sharedData) {

                    val.set(sharedData.getAndIncrement());
                    return val.get() < 10;
                }
            }, () -> {
                return new IDataHolder.CallTrailer();
            });
            if(i<10) {
                assertTrue(res);
            }
            else
                assertFalse(res);
            assertEquals(i, val.get());
        }
    }

}