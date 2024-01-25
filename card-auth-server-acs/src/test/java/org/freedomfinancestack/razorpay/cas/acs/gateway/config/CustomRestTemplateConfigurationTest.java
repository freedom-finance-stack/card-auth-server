package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomRestTemplateConfigurationTest {

    // todo isSSL true case

    @Test
    public void visaTemplate_Success()
            throws UnrecoverableKeyException,
                    CertificateException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    IOException,
                    KeyManagementException {

        // Arrange
        GatewayConfig.ServiceConfig visaConfig = new GatewayConfig.ServiceConfig();
        visaConfig.setUseSSL(false);
        visaConfig.setConnectTimeout(3000);
        visaConfig.setResponseTimeout(3000);
        visaConfig.setKeyStore(new GatewayConfig.KeyStoreConfig());
        visaConfig.setTrustStore(new GatewayConfig.TrustStoreConfig());
        visaConfig.setRetryable(new GatewayConfig.RetryConfig());

        Map<ClientType, GatewayConfig.ServiceConfig> services = new EnumMap<>(ClientType.class);
        services.put(ClientType.VISA_DS, visaConfig);

        GatewayConfig gatewayConfig = new GatewayConfig();
        gatewayConfig.setServices(services);

        CustomRestTemplateConfiguration customRestTemplateConfiguration =
                new CustomRestTemplateConfiguration(gatewayConfig, new AppConfiguration());

        // Act
        RestTemplate restTemplate = customRestTemplateConfiguration.visaRestTemplate();

        // Assert
        assertNotNull(restTemplate);
    }

    @Test
    public void masterCard_SuccessCase()
            throws UnrecoverableKeyException,
                    CertificateException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    IOException,
                    KeyManagementException {
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        serviceConfig.setMock(false);
        serviceConfig.setUrl("https://example.com");
        serviceConfig.setUseSSL(false);
        serviceConfig.setConnectTimeout(3000);
        serviceConfig.setResponseTimeout(3000);
        serviceConfig.setKeyStore(new GatewayConfig.KeyStoreConfig());
        serviceConfig.setTrustStore(new GatewayConfig.TrustStoreConfig());
        serviceConfig.setRetryable(new GatewayConfig.RetryConfig());

        GatewayConfig gatewayConfig = new GatewayConfig();
        gatewayConfig.getServices().put(ClientType.MASTERCARD_DS, serviceConfig);

        CustomRestTemplateConfiguration configuration =
                new CustomRestTemplateConfiguration(gatewayConfig, new AppConfiguration());

        RestTemplate restTemplate = configuration.masterCardRestTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    public void threedsRequestorRestTemplate_Success()
            throws UnrecoverableKeyException,
                    CertificateException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    IOException,
                    KeyManagementException {
        // Arrange
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        serviceConfig.setUseSSL(false);
        serviceConfig.setConnectTimeout(5000);
        serviceConfig.setResponseTimeout(5000);
        GatewayConfig gatewayConfig = new GatewayConfig();
        gatewayConfig.getServices().put(ClientType.THREEDS_REQUESTOR_SERVER, serviceConfig);
        CustomRestTemplateConfiguration configuration =
                new CustomRestTemplateConfiguration(gatewayConfig, new AppConfiguration());

        // Act
        RestTemplate restTemplate = configuration.threedsRequestorRestTemplate();

        // Assert
        assertNotNull(restTemplate);
    }

    @Test
    public void ulTest_Success()
            throws UnrecoverableKeyException,
                    CertificateException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    IOException,
                    KeyManagementException {
        // Arrange
        GatewayConfig.ServiceConfig serviceConfig = new GatewayConfig.ServiceConfig();
        serviceConfig.setUseSSL(false);
        serviceConfig.setConnectTimeout(5000);
        serviceConfig.setResponseTimeout(5000);
        GatewayConfig gatewayConfig = new GatewayConfig();
        gatewayConfig.getServices().put(ClientType.UL_TEST_PORTAL, serviceConfig);
        CustomRestTemplateConfiguration configuration =
                new CustomRestTemplateConfiguration(gatewayConfig, new AppConfiguration());

        // Act
        RestTemplate restTemplate = configuration.ulTestRestTemplate();

        // Assert
        assertNotNull(restTemplate);
    }
}
