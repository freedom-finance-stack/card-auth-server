package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomRestTemplateConfiguration {

    private final DsGatewayConfig dsGatewayConfig;

    @Bean("visaDsRestTemplate")
    public RestTemplate visaRestTemplate()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException {
        DsGatewayConfig.ServiceConfig visaConfig =
                dsGatewayConfig.getServices().get(ClientType.VISA_DS);

        return getRestTemplate(visaConfig);
    }

    @Bean("masterCardDsRestTemplate")
    public RestTemplate masterCardRestTemplate()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException {
        DsGatewayConfig.ServiceConfig masterCardConfig =
                dsGatewayConfig.getServices().get(ClientType.MASTERCARD_DS);
        return getRestTemplate(masterCardConfig);
    }

    private RestTemplate getRestTemplate(DsGatewayConfig.ServiceConfig config)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
                    CertificateException, IOException {
        HttpClientBuilder httpClient = HttpClients.custom();
        if (config.isUseSSL()) {
            SSLContext sslContext =
                    new SSLContextBuilder()
                            .loadTrustMaterial(
                                    config.getKeyStore().getPath().getURL(),
                                    config.getKeyStore().getPassword().toCharArray())
                            .build();
            SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
            httpClient.setSSLSocketFactory(sslConFactory);
        }
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient.build());
        requestFactory.setConnectTimeout(config.getConnectTimeout());
        requestFactory.setReadTimeout(config.getReadTimeout());
        return new RestTemplate(requestFactory);
    }
}
