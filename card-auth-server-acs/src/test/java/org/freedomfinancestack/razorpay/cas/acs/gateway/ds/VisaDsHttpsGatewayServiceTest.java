package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.junit.jupiter.api.Test;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class VisaDsHttpsGatewayServiceTest {

    @Test
    void visaDsHttpsGatewayService() {
        RestTemplate visaDsRestTemplate = new RestTemplate();
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.VISA_DS, serviceConfig));
        GatewayConfig.RetryConfig retryTemplate = new GatewayConfig.RetryConfig();
        retryTemplate.setBackOffPeriod(1L);
        retryTemplate.setMaxAttempts(1);
        serviceConfig.setRetryable(retryTemplate);

        VisaDsHttpsGatewayService visaDsHttpsGatewayService =
                new VisaDsHttpsGatewayService(visaDsRestTemplate, gatewayConfig);
        RetryTemplate actualRetryTemplate = visaDsHttpsGatewayService.getRetryTemplate();

        assertNotNull(visaDsHttpsGatewayService);
        assertEquals(serviceConfig, visaDsHttpsGatewayService.getServiceConfig());
        assertEquals(visaDsRestTemplate, visaDsHttpsGatewayService.getRestTemplate());
        assertNotNull(actualRetryTemplate);
    }
}
