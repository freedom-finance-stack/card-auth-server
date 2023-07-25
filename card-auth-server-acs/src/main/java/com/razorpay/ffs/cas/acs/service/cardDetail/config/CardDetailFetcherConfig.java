package com.razorpay.ffs.cas.acs.service.cardDetail.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.ffs.cas.acs.service.cardDetail.CardDetailFetcherFactory;

@Configuration
public class CardDetailFetcherConfig {
    @Bean("cardDetailFetcherFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(CardDetailFetcherFactory.class);
        return factoryBean;
    }
}
