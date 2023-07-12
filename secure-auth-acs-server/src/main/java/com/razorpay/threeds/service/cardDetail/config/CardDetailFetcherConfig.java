package com.razorpay.threeds.service.cardDetail.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.threeds.service.cardDetail.CardDetailFetcherFactory;

@Configuration
public class CardDetailFetcherConfig {
    @Bean("cardDetailFetcherFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(CardDetailFetcherFactory.class);
        return factoryBean;
    }
}
