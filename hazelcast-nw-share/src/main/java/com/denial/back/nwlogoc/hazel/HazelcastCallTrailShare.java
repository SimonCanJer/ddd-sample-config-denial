package com.denial.back.nwlogoc.hazel;

import com.back.api.IDataHolder;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

public class HazelcastCallTrailShare implements IDataHolder {
    IMap<Integer, CallTrailer> map;
    private Config mConfig;
    private HazelcastInstance mHazelcast;
    static final String MAP="quotes";


    void initConfig()
    {
        mConfig= new Config();
        mConfig.setInstanceName(UUID.randomUUID().toString());
        mConfig.getGroupConfig().setPassword("denial");
        mConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true).setMulticastGroup("224.125.112.111");
    }
    private void configMap(String name) {
        MapConfig cfg = new MapConfig();
        cfg.setName(name);
        mConfig.addMapConfig(cfg);
    }
    void createInstances()
    {
        try {
            mHazelcast = Hazelcast.newHazelcastInstance(mConfig);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Error(e);
        }
        map=mHazelcast.getMap(MAP);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            mHazelcast.shutdown();}));
    }
    @Override
    public boolean proceedRequest(int id, Function<CallTrailer,Boolean> decider, Supplier<CallTrailer> onNull) {
        AtomicBoolean res = new AtomicBoolean(false);
//Hazelcast locks the key in NW
        return (boolean)map.executeOnKey(id, new EntryProcessor() {
            @Override
            public Object process(Map.Entry entry) {
                CallTrailer actual = (CallTrailer) entry.getValue();
                if(actual==null)
                    actual= onNull.get();
                res.set(decider.apply(actual));
                entry.setValue(actual);
                return res.get();
            }
           @Override
            public EntryBackupProcessor getBackupProcessor() {
                return null;
            }
        });



    }

    @Override
    public int registeredClients() {
        return map.size();
    }

    @Override
    public IDataHolder init() {
        initConfig();
        configMap(MAP);
        createInstances();
        return this;
    }

    @Override
    public IDataHolder shutdown() {
        mHazelcast.shutdown();
        return this;
    }
}
