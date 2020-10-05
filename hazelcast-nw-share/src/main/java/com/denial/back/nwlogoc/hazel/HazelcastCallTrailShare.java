package com.denial.back.nwlogoc.hazel;

import com.back.api.IDataHolder;
import com.back.api.IPipeline;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

/**
 * Hazelcast based implementation of data service.
 * Remark. It uses the build in method of network safity of execution of a delegated pipeline
 * request.
 * @see IMap#executeOnKey(Object, EntryProcessor)
 * @see IPipeline
 * @see #proceedRequest(String, IPipeline, BiConsumer, Supplier, String)
 */
public class HazelcastCallTrailShare implements IDataHolder {
    IMap<String, CallTrailer> map;
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
    public VarResult proceedRequest(String id, IPipeline pipeline, BiConsumer<Map<String, Serializable>, String> operation, Supplier<CallTrailer> onNull, String command) {
        AtomicReference<VarResult> res= new AtomicReference<>();
//Hazelcast locks the key in NW
        return (VarResult) map.executeOnKey(id, new EntryProcessor() {
            @Override
            public Object process(Map.Entry entry) {
                CallTrailer actual = (CallTrailer) entry.getValue();
                if(actual==null)
                    actual= onNull.get();
                res.set(pipeline.apply(actual,operation,command));
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
