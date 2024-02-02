package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatewayConfigTest {

    @Test
    public void gatewayConfig() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        assertNotNull(gatewayConfig);
    }

    @Test
    public void serviceConfig_with_diffValues() {
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        serviceConfig.setMock(true);
        serviceConfig.setUrl("https://example.com");
        serviceConfig.setUseSSL(true);
        serviceConfig.setConnectTimeout(5000);
        serviceConfig.setResponseTimeout(5000);
        serviceConfig.setKeyStore(new GatewayConfig.KeyStoreConfig());
        serviceConfig.setTrustStore(new GatewayConfig.TrustStoreConfig());
        serviceConfig.setRetryable(new GatewayConfig.RetryConfig());

        Map<ClientType, GatewayConfig.ServiceConfig> services = new EnumMap<>(ClientType.class);
        services.put(ClientType.VISA_DS, serviceConfig);

        GatewayConfig gatewayConfig = new GatewayConfig();
        gatewayConfig.setServices(services);

        assertNotNull(gatewayConfig);
    }

    @Test
    public void gateway_with_empty_services_map() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        assertNotNull(gatewayConfig.getServices());
        assertTrue(gatewayConfig.getServices().isEmpty());
    }

    @Test
    public void serviceConfig_instantiation_with_null_values() {
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        assertNull(serviceConfig.getUrl());
        assertNull(serviceConfig.getKeyStore());
        assertNull(serviceConfig.getTrustStore());
        assertNull(serviceConfig.getRetryable());
    }

    @Test
    public void test_KeyStoreConfig_instantiation_with_null_values() {
        GatewayConfig.KeyStoreConfig keyStoreConfig = new GatewayConfig.KeyStoreConfig();
        assertNull(keyStoreConfig.getPath());
        assertNull(keyStoreConfig.getPassword());
    }

    @Test
    public void service_config_default_values() {
        // Arrange
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();

        // Assert
        assertFalse(serviceConfig.isMock());
        assertNull(serviceConfig.getUrl());
        assertFalse(serviceConfig.isUseSSL());
        assertEquals(Integer.valueOf(3000), serviceConfig.getConnectTimeout());
        assertEquals(Integer.valueOf(3000), serviceConfig.getResponseTimeout());
        assertNull(serviceConfig.getKeyStore());
        assertNull(serviceConfig.getTrustStore());
        assertNull(serviceConfig.getRetryable());
    }

    @Test
    void retryConfig_objectConstructor() {
        GatewayConfig.RetryConfig retryConfig = new GatewayConfig.RetryConfig();
        retryConfig.setMaxAttempts(1);
        retryConfig.setBackOffPeriod(1L);
        GatewayConfig.RetryConfig retryConfig1 = new GatewayConfig.RetryConfig(retryConfig);
        assertEquals(retryConfig1.getMaxAttempts(), retryConfig.getMaxAttempts());
        assertEquals(retryConfig1.getBackOffPeriod(), retryConfig.getBackOffPeriod());
    }

    @Test
    void serviceConfig_objectConstrctor() {
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        serviceConfig.setUrl("dummy");
        serviceConfig.setMock(false);
        serviceConfig.setUseSSL(false);
        serviceConfig.setConnectTimeout(1);
        serviceConfig.setResponseTimeout(1);
        GatewayConfig.RetryConfig retryConfig = new GatewayConfig.RetryConfig();
        serviceConfig.setRetryable(retryConfig);
        GatewayConfig.KeyStoreConfig keyStoreConfig = new GatewayConfig.KeyStoreConfig();
        serviceConfig.setKeyStore(keyStoreConfig);
        GatewayConfig.TrustStoreConfig trustStoreConfig = new GatewayConfig.TrustStoreConfig();
        serviceConfig.setTrustStore(trustStoreConfig);

        GatewayConfig.ServiceConfig serviceConfig1 = new GatewayConfig.ServiceConfig(serviceConfig);
        // ASSERT
        assertEquals(serviceConfig.isMock(), serviceConfig1.isMock());
        assertEquals(serviceConfig.getUrl(), serviceConfig1.getUrl());
        assertEquals(serviceConfig.getRetryable(), retryConfig);
        assertEquals(serviceConfig.isUseSSL(), serviceConfig1.isUseSSL());
        assertEquals(serviceConfig.isMock(), serviceConfig1.isMock());
        assertEquals(serviceConfig.getConnectTimeout(), serviceConfig1.getConnectTimeout());
        assertEquals(serviceConfig.getResponseTimeout(), serviceConfig1.getResponseTimeout());
        assertEquals(serviceConfig.getKeyStore(), keyStoreConfig);
        assertEquals(serviceConfig.getTrustStore(), trustStoreConfig);
    }
}
