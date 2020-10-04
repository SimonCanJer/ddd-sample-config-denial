package com.denial.back.config;

import com.back.api.IDataHolder;
import com.back.api.IDomain;
import com.back.domain.ConcreteDomainOpenInject;
import com.denial.back.jersey.JerseyController;
import com.denial.back.nwlogoc.hazel.HazelcastCallTrailShare;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class Config extends ResourceConfig {
    public Config() {
        register(new Binder());
        register(new JerseyController());

    }
    static class ConcreteDomainImpl extends ConcreteDomainOpenInject
    {
       /* @Inject*/
        IDataHolder holder;
        ConcreteDomainImpl(IDataHolder holder)
        {
            this.holder=holder;
        }
        @Override
        protected IDataHolder getDataPopulator() {
            return holder;
        }
    }

    private static final AtomicReference<IDataHolder> holder= new AtomicReference<>();

    public static class DomainSupplier implements Supplier<IDomain>, Factory<IDomain> {
        static AtomicReference<IDomain> instanceHolder= new AtomicReference<>();
        @Override
        public IDomain get() {
            if(instanceHolder.get()==null)
            {
                synchronized (instanceHolder)
                {
                    if(instanceHolder.get()==null)
                    {
                        instanceHolder.set(new ConcreteDomainImpl(holder.get()));
                    }
                }
            }
            return instanceHolder.get();
        }

        @Override
        public IDomain provide() {
            return get();
        }

        @Override
        public void dispose(IDomain iDomain) {

        }
    }

    public static class Binder extends AbstractBinder {
        @Override
        protected void configure() {


            bindFactory(DomainSupplier.class).to(IDomain.class);
            holder.set(new HazelcastCallTrailShare().init());

        }
    }
}
