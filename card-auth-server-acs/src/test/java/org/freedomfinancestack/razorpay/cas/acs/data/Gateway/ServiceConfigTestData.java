package org.freedomfinancestack.razorpay.cas.acs.data.Gateway;

import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;

public class ServiceConfigTestData {
    public static GatewayConfig.ServiceConfig createServiceConfig() {

        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        serviceConfig.setMock(false);
        serviceConfig.setUrl("SampleUrl");
        serviceConfig.setUseSSL(false);
        serviceConfig.setConnectTimeout(1);
        serviceConfig.setResponseTimeout(1);
        GatewayConfig.TrustStoreConfig trustStoreConfig = new GatewayConfig.TrustStoreConfig();
        trustStoreConfig.setPassword("SamplePassword");
        trustStoreConfig.setPath("SamplePath");
        serviceConfig.setTrustStore(trustStoreConfig);
        GatewayConfig.KeyStoreConfig keyStoreConfig = new GatewayConfig.KeyStoreConfig();
        keyStoreConfig.setPath("SamplePath");
        keyStoreConfig.setPassword("SamplePassword");
        serviceConfig.setKeyStore(keyStoreConfig);
        GatewayConfig.RetryConfig retryConfig = new GatewayConfig.RetryConfig();
        retryConfig.setBackOffPeriod(1L);
        retryConfig.setBackOffPeriod(1L);
        serviceConfig.setRetryable(retryConfig);

        return serviceConfig;
    }
}
