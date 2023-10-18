package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.util.Timeout;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
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
                    IOException,
                    UnrecoverableKeyException {
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
                    IOException,
                    UnrecoverableKeyException {
        DsGatewayConfig.ServiceConfig masterCardConfig =
                dsGatewayConfig.getServices().get(ClientType.MASTERCARD_DS);
        return getRestTemplate(masterCardConfig);
    }

    private RestTemplate getRestTemplate(DsGatewayConfig.ServiceConfig config)
            throws NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException,
                    UnrecoverableKeyException,
                    KeyManagementException {

        SSLConnectionSocketFactory sslConFactory =
                new SSLConnectionSocketFactory(
                        createSSLContext(
                                String.valueOf(config.getKeyStore().getPath()),
                                config.getKeyStore().getIdentifier()));
        HttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(sslConFactory)
                        .build();

        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(config.getConnectTimeout()))
                        .setResponseTimeout(Timeout.ofMilliseconds(config.getResponseTimeout()))
                        .build();

        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setConnectionManager(connectionManager)
                        .setDefaultRequestConfig(requestConfig)
                        .build();
        ClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    public static SSLContext createSSLContext(String keyStorePath, String keyStorePassword)
            throws KeyStoreException,
                    IOException,
                    NoSuchAlgorithmException,
                    CertificateException,
                    UnrecoverableKeyException,
                    KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keyStoreStream = new FileInputStream(keyStorePath)) {
            keyStore.load(keyStoreStream, keyStorePassword.toCharArray());
        }

        KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        return sslContext;
    }
}
