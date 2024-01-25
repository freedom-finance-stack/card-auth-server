package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import java.util.Map;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.gateway.threedsrequestor.ThreedsRequestorCResService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.Gateway;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import static org.freedomfinancestack.razorpay.cas.acs.data.Gateway.ServiceConfigTestData.createServiceConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlrqServiceTest {
     @Mock
     private AppConfiguration appConfiguration;
     @Mock
     private RestTemplate restTemplate;

    @Test
    void sendPlrq_Happy_path(){
        //todo (send request() )
    }

    @Test
    void getRestTemplate() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        PlrqService plrqService = new PlrqService(appConfiguration,restTemplate, gatewayConfig);

        RestTemplate actualRestTemplate1 = plrqService.getRestTemplate();
        assertNotNull(actualRestTemplate1);
        assertEquals(restTemplate, actualRestTemplate1);
    }

    @Test
    void getRetryTemplate() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        PlrqService plrqService = new PlrqService(appConfiguration,restTemplate, gatewayConfig);

        RetryTemplate expectedRetryTemplateBuilder =
                new CustomRetryTemplateBuilder()
                        .withRetryMaxAttempts(1)
                        .withBackOffPeriod(1)
                        .withHttpStatus(HttpStatus.BAD_GATEWAY)
                        .withHttpStatus(HttpStatus.GATEWAY_TIMEOUT)
                        .withHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                        .build();


        RetryTemplate actualRetryTemplateBuilder = plrqService.getRetryTemplate();
        assertNotNull(actualRetryTemplateBuilder);
    }
    @Test
    void getServiceConfig() {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        PlrqService plrqService = new PlrqService(appConfiguration,restTemplate, gatewayConfig);

        assertEquals(
                gatewayConfig.getServices().get(ClientType.UL_TEST_PORTAL),
                plrqService.getServiceConfig());
    }

    @Test
    void sendPlrq_isMock() throws ACSValidationException {
        GatewayConfig gatewayConfig = new GatewayConfig();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        gatewayConfig.setServices(Map.of(ClientType.UL_TEST_PORTAL, serviceConfig));
        PlrqService plrqService = new PlrqService(appConfiguration,restTemplate, gatewayConfig);
        new AesEncryptor(NoOpEncryption.INSTANCE);
        Transaction transaction = TransactionTestData.createSampleAppTransaction();
        serviceConfig.setMock(true);
        plrqService.sendPlrq(String.valueOf(transaction), "1222", "1.0", "123", "App-based");
    }


}
