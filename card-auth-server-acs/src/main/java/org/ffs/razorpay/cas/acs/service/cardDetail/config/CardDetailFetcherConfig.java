package org.ffs.razorpay.cas.acs.service.cardDetail.config;

import org.ffs.razorpay.cas.acs.service.cardDetail.CardDetailFetcherFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code CardDetailFetcherConfig} class provides the configuration for creating a factory bean
 * to dynamically locate and retrieve instances of {@link CardDetailFetcherFactory}.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Configuration
public class CardDetailFetcherConfig {

    /**
     * Creates a factory bean for dynamically locating and retrieving instances of {@link
     * CardDetailFetcherFactory}.
     *
     * @return the factory bean for {@link CardDetailFetcherFactory}
     */
    @Bean("cardDetailFetcherFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(CardDetailFetcherFactory.class);
        return factoryBean;
    }
}
