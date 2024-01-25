package org.freedomfinancestack.razorpay.cas.acs.gateway.threedsrequestor;

import java.util.Map;

import org.freedomfinancestack.extensions.crypto.NoOpEncryption;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.dao.encryption.AesEncryptor;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import static org.freedomfinancestack.razorpay.cas.acs.data.Gateway.ServiceConfigTestData.createServiceConfig;
import static org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData.createSampleAppTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ThreedsRequestorCResServiceTest {
    @Mock RestTemplate threedsRequestorRestTemplate;
    GatewayConfig gatewayConfig = new GatewayConfig();

    @Test
    void sendNotificationCRes_Success() throws ACSValidationException {
        new AesEncryptor(NoOpEncryption.INSTANCE);
        Transaction transaction = createSampleAppTransaction();
        GatewayConfig.ServiceConfig serviceConfig = createServiceConfig();
        // todo ismock() true for 2-3 more line coverage

        gatewayConfig.setServices(Map.of(ClientType.THREEDS_REQUESTOR_SERVER, serviceConfig));
        ThreedsRequestorCResService threedsRequestorCResService =
                new ThreedsRequestorCResService(threedsRequestorRestTemplate, gatewayConfig);
        threedsRequestorCResService.sendNotificationCRes(transaction);

        // RestTemplate testing
        RestTemplate actualrestTemplate = threedsRequestorCResService.getRestTemplate();
        assertEquals(threedsRequestorRestTemplate, actualrestTemplate);

        // getServiceConfig testcase
        GatewayConfig.ServiceConfig actualServiceConfig =
                threedsRequestorCResService.getServiceConfig();
        assertEquals("http://notificationUrl.com", actualServiceConfig.getUrl());

        // getRetryTemplate testcase
        RetryTemplate retryTemplate = threedsRequestorCResService.getRetryTemplate();
        assertNotNull(retryTemplate);
    }
}
