package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
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
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException {
        DsGatewayConfig.ServiceConfig visaConfig =
                dsGatewayConfig.getServices().get(ClientType.VISA_DS);

        return getRestTemplate(visaConfig);
    }

    @Bean("masterCardDsRestTemplate")
    public RestTemplate masterCardRestTemplate()
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException {
        DsGatewayConfig.ServiceConfig masterCardConfig =
                dsGatewayConfig.getServices().get(ClientType.MASTERCARD_DS);
        return getRestTemplate(masterCardConfig);
    }

    private RestTemplate getRestTemplate(DsGatewayConfig.ServiceConfig config)
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException {

        PoolingHttpClientConnectionManagerBuilder clientConnectionManagerBuilder =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setDefaultSocketConfig(
                                SocketConfig.custom()
                                        .setSoTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                                        .build())
                        .setDefaultConnectionConfig(
                                ConnectionConfig.custom()
                                        .setConnectTimeout(
                                                config.getConnectTimeout(), TimeUnit.SECONDS)
                                        .build());
        if (config.isUseSSL()) {
            SSLContext sslContext =
                    new SSLContextBuilder()
                            .loadTrustMaterial(
                                    config.getKeyStore().getPath().getURL(),
                                    config.getKeyStore().getPassword().toCharArray())
                            .build();
            final SSLConnectionSocketFactory sslsf =
                    new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            clientConnectionManagerBuilder.setSSLSocketFactory(sslsf);
        }

        PoolingHttpClientConnectionManager clientConnectionManager =
                clientConnectionManagerBuilder.build();

        final CloseableHttpClient httpClient =
                HttpClients.custom().setConnectionManager(clientConnectionManager).build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }
}
