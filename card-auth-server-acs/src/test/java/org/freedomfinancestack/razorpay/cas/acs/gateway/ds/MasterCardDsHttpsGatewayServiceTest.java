package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.junit.jupiter.api.Test;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class MasterCardDsHttpsGatewayServiceTest {
    @Test
    void masterCardDsHttpsGatewayService() {
        RestTemplate masterCardDsRestTemplate = new RestTemplate();
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.MASTERCARD_DS, serviceConfig));
        GatewayConfig.RetryConfig retryTemplate = new GatewayConfig.RetryConfig();
        retryTemplate.setBackOffPeriod(1L);
        retryTemplate.setMaxAttempts(1);
        serviceConfig.setRetryable(retryTemplate);

        MasterCardDsHttpsGatewayService masterCardDsHttpsGatewayService =
                new MasterCardDsHttpsGatewayService(masterCardDsRestTemplate, gatewayConfig);
        RetryTemplate actualRetryTemplate = masterCardDsHttpsGatewayService.getRetryTemplate();

        assertNotNull(masterCardDsHttpsGatewayService);
        assertEquals(serviceConfig, masterCardDsHttpsGatewayService.getServiceConfig());
        assertEquals(masterCardDsRestTemplate, masterCardDsHttpsGatewayService.getRestTemplate());
        assertNotNull(actualRetryTemplate);
    }
}
