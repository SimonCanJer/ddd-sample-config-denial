package com.back.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

/**
 * The interface defines delegated operations with data and main data classes which used
 * for data caching/persistenced and data export
 */
public interface IDataHolder {

    class CallTrailer implements Serializable {
        public int getCounter() {
            return counter;
        }

        public long getLastAccess() {
            return lastAccess;
        }

        int counter = 0;
        long lastAccess, birthTime = System.currentTimeMillis();

        public Map<String, Serializable> getData() {
            return data;
        }

        Map<String, Serializable> data= new HashMap<>();

        public void setInitCounter(int counter) {
            this.counter = counter;
            lastAccess = birthTime=System.currentTimeMillis();
        }

        public int incrementAndGet() {
            return ++counter;
        }

        public int  getAndIncrement() {
            try {
                return counter;
            } finally {
                counter++;

            }
        }

        public void setLastAccess(long lastAccess) {
            this.lastAccess = lastAccess;
        }

        public long getBirthTime() {
            return birthTime;
        }

    }
    class VarResult implements Serializable
    {
        public Map<String, Serializable> getNextValue() {
            return nextValue;
        }

        public boolean isResult() {
            return result;
        }

        public String getError() {
            return error;
        }

        final Map<String,Serializable> nextValue;
        final boolean result;
        String error;

        public VarResult(CallTrailer nextValue) {
            this.nextValue = nextValue.data;
            this.result = true;
            error=null;
        }
        public VarResult() {
            this.nextValue =null;
            this.result = false;
            this.error="out of quote";
        }
        public VarResult(String error) {
            this();
            this.error=error;
        }
        public boolean success()
        {
            return result&& error==null;
        }

    }

   VarResult proceedRequest(String id, IPipeline pipeline, BiConsumer<Map<String, Serializable>, String> operation, Supplier<CallTrailer> onNull, String command);
    int registeredClients();
    IDataHolder init();
    IDataHolder shutdown();

}
