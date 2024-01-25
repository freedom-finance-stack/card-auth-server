package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import java.util.Map;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import static org.freedomfinancestack.razorpay.cas.acs.data.Gateway.ServiceConfigTestData.createServiceConfig;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class POrqServiceTest {
    @Mock RestTemplate restTemplate;

    @Test
    void sendPOrq_Happy_path() {
        // todo (send request() )
    }

    @Test
    void sendPOrq_isMock() throws ACSValidationException {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        serviceConfig.setMock(true);
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        POrqService pOrqService = new POrqService(restTemplate, gatewayConfig);
        new AesEncryptor(NoOpEncryption.INSTANCE);
        Transaction transaction = TransactionTestData.createSampleAppTransaction();

        pOrqService.sendPOrq(transaction.getId(), "1222", "1.0");
    }

    @Test
    void getServiceConfig() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        POrqService pOrqService = new POrqService(restTemplate, gatewayConfig);
        GatewayConfig.ServiceConfig actualServiceConfig = pOrqService.getServiceConfig();
        assertNotNull(actualServiceConfig);
        assertEquals(
                gatewayConfig.getServices().get(ClientType.UL_TEST_PORTAL).getUrl(),
                actualServiceConfig.getUrl());
    }

    @Test
    void getRestTemplate() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        RestTemplate visaMockRestTemplate = mock(RestTemplate.class);
        POrqService pOrqService = new POrqService(visaMockRestTemplate, gatewayConfig);

        RestTemplate actualRestTemplate1 = pOrqService.getRestTemplate();
        assertNotNull(actualRestTemplate1);
    }

    @Test
    void getRetryTemplate() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        POrqService pOrqService = new POrqService(restTemplate, gatewayConfig);

        RetryTemplate expectedRetryTemplateBuilder =
                new CustomRetryTemplateBuilder()
                        .withRetryMaxAttempts(1)
                        .withBackOffPeriod(1)
                        .withHttpStatus(HttpStatus.BAD_GATEWAY)
                        .withHttpStatus(HttpStatus.GATEWAY_TIMEOUT)
                        .withHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                        .build();

        RetryTemplate actualRetryTemplateBuilder = pOrqService.getRetryTemplate();
        assertNotNull(actualRetryTemplateBuilder);
    }
}
