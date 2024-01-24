package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class CardDetailFetcherConfigTest {

    CardDetailFetcherConfig cardDetailFetcherConfig = new CardDetailFetcherConfig();

    @Test
    public void serviceLocatorFactoryBean() {
        FactoryBean factoryBean = cardDetailFetcherConfig.serviceLocatorFactoryBean();
        assertNotNull(factoryBean);
    }
}
