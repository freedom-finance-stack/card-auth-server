package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomRestTemplateConfiguration {

    private final GatewayConfig gatewayConfig;

    @Bean("visaDsRestTemplate")
    public RestTemplate visaRestTemplate()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException {
        GatewayConfig.ServiceConfig visaConfig =
                gatewayConfig.getServices().get(ClientType.VISA_DS);
        return getRestTemplate(visaConfig);
    }

    @Bean("masterCardDsRestTemplate")
    public RestTemplate masterCardRestTemplate()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException {
        GatewayConfig.ServiceConfig masterCardConfig =
                gatewayConfig.getServices().get(ClientType.MASTERCARD_DS);
        return getRestTemplate(masterCardConfig);
    }

    private RestTemplate getRestTemplate(GatewayConfig.ServiceConfig config)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException {
        SSLContext sslContext =
                new SSLContextBuilder()
                        .loadTrustMaterial(
                                config.getKeyStore().getPath().getURL(),
                                config.getKeyStore().getPassword().toCharArray())
                        .build();
        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient =
                HttpClients.custom().setSSLSocketFactory(sslConFactory).build();
        ClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
