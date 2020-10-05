package com.denial.back.spring;


import com.back.api.IDataHolder;
import com.back.api.IDomain;
import com.back.domain.ConcreteDomainOpenInject;
import com.denial.back.nwlogoc.hazel.HazelcastCallTrailShare;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring inherent configuration of business logic
 */
@SpringBootApplication
@EnableWebMvc
public class SpringConfig {
    private static ApplicationContext theContext;

    static public ApplicationContext getContext()
    {
        return theContext;
    }
    static void start()
    {
        SpringApplication.run(SpringConfig.class);
    }
    static class ConcreteDomainImpl extends ConcreteDomainOpenInject
    {
        @Autowired
        IDataHolder holder;

        @Override
        protected IDataHolder getDataPopulator() {
            return holder;
        }
    }
    @Bean
    IDataHolder sharedData()
    {
        return new HazelcastCallTrailShare().init();
    }
    @Bean
    IDomain domain()
    {
        return new ConcreteDomainImpl();
    }

    @Bean
    static ApplicationContextAware onContextReady()
    {
       return new ApplicationContextAware() {
           @Override
           public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
               theContext = applicationContext;
           }
       };

    }
}
