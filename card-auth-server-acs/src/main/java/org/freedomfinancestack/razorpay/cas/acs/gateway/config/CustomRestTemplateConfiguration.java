package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.util.Timeout;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
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
    private final AppConfiguration appConfiguration;

    @Bean("visaDsRestTemplate")
    public RestTemplate visaRestTemplate()
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException,
                    UnrecoverableKeyException {
        GatewayConfig.ServiceConfig visaConfig =
                gatewayConfig.getServices().get(ClientType.VISA_DS);
        return getRestTemplate(ClientType.VISA_DS, visaConfig);
    }

    @Bean("masterCardDsRestTemplate")
    public RestTemplate masterCardRestTemplate()
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException,
                    UnrecoverableKeyException {
        GatewayConfig.ServiceConfig masterCardConfig =
                gatewayConfig.getServices().get(ClientType.MASTERCARD_DS);
        return getRestTemplate(ClientType.MASTERCARD_DS, masterCardConfig);
    }

    @Bean("ulTestRestTemplate")
    public RestTemplate ulTestRestTemplate()
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException,
                    UnrecoverableKeyException {
        GatewayConfig.ServiceConfig ulTestConfig =
                gatewayConfig.getServices().get(ClientType.UL_TEST_PORTAL);
        return getRestTemplate(ClientType.UL_TEST_PORTAL, ulTestConfig);
    }

    @Bean("threedsRequestorRestTemplate")
    public RestTemplate threedsRequestorRestTemplate()
            throws KeyManagementException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException,
                    UnrecoverableKeyException {
        GatewayConfig.ServiceConfig threedsRequestorConfig =
                gatewayConfig.getServices().get(ClientType.THREEDS_REQUESTOR_SERVER);
        return getRestTemplate(ClientType.THREEDS_REQUESTOR_SERVER, threedsRequestorConfig);
    }

    private RestTemplate getRestTemplate(ClientType clientType, GatewayConfig.ServiceConfig config)
            throws NoSuchAlgorithmException,
                    KeyStoreException,
                    CertificateException,
                    IOException,
                    UnrecoverableKeyException,
                    KeyManagementException {
        PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder =
                PoolingHttpClientConnectionManagerBuilder.create();

        if (config.isUseSSL()) {
            createTrustStore(
                    clientType,
                    config.getTrustStore().getPath(),
                    getCacertsPath(),
                    config.getTrustStore().getPassword());
            SSLConnectionSocketFactory sslConFactory =
                    new SSLConnectionSocketFactory(
                            createSSLContext(
                                    String.valueOf(config.getKeyStore().getPath()),
                                    config.getKeyStore().getPassword()));
            connectionManagerBuilder.setSSLSocketFactory(sslConFactory);
        }

        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setResponseTimeout(Timeout.ofMilliseconds(config.getResponseTimeout()))
                        .build();

        ConnectionConfig connectionConfig =
                ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(config.getConnectTimeout()))
                        .build();

        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setConnectionManager(
                                connectionManagerBuilder
                                        .setDefaultConnectionConfig(connectionConfig)
                                        .build())
                        .setDefaultRequestConfig(requestConfig)
                        .build();

        ClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    private static SSLContext createSSLContext(String keyStorePath, String keyStorePassword)
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

    private static void createTrustStore(
            ClientType clientType,
            String trustStoreSourcePath,
            String truststoreDestPath,
            String trustStorePassword)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream truststoreFile = new FileInputStream(truststoreDestPath);
        KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
        truststore.load(truststoreFile, trustStorePassword.toCharArray());

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try (InputStream caCertFile = new FileInputStream(trustStoreSourcePath)) {
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(caCertFile);
            truststore.setCertificateEntry(
                    clientType.name() + "_DSCert", caCert); // Provide an alias for the certificate
            // Save the updated truststore
            try (FileOutputStream truststoreOutputStream =
                    new FileOutputStream(truststoreDestPath)) {
                truststore.store(truststoreOutputStream, trustStorePassword.toCharArray());
            }
        }
    }

    private String getCacertsPath() {
        if (!Util.isNullorBlank(appConfiguration.getJava().getCacerts())) {
            return appConfiguration.getJava().getCacerts();
        }
        return appConfiguration.getJava().getHome() + "/lib/security/cacerts";
    }
}
