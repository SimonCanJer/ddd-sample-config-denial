package com.back.api;

import java.io.Serializable;
import java.util.function.*;

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

    boolean proceedRequest(int id, Function<CallTrailer, Boolean> decider, Supplier<CallTrailer> onNull);
    int registeredClients();
    IDataHolder init();
    IDataHolder shutdown();

}
