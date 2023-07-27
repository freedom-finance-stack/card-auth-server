package org.ffs.razorpay.cas.acs.service.cardDetail.config;

import org.ffs.razorpay.cas.acs.service.cardDetail.CardDetailFetcherFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardDetailFetcherConfig {
    @Bean("cardDetailFetcherFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(CardDetailFetcherFactory.class);
        return factoryBean;
    }
}
