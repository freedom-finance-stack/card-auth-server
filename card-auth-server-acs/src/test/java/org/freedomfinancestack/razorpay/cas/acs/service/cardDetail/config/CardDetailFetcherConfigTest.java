package org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class CardDetailFetcherConfigTest {

    CardDetailFetcherConfig cardDetailFetcherConfig = new CardDetailFetcherConfig();

    @Test
    public void test_creates_new_instance() { // TODO
        FactoryBean factoryBean = cardDetailFetcherConfig.serviceLocatorFactoryBean();
        assertNotNull(factoryBean);
    }
}
