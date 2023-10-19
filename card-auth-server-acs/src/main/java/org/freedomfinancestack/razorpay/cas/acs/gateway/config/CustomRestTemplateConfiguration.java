package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.util.Timeout;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomRestTemplateConfiguration {
    public final static String SSL_java_protocol_handler_pkgs = "sun.net.www.protocol";
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

        return getRestTemplate(Network.VISA, visaConfig);
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
        return getRestTemplate(Network.MASTERCARD, masterCardConfig);
    }

    private RestTemplate getRestTemplate(Network network, DsGatewayConfig.ServiceConfig config)
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
                    network,
                    config.getTrustStore().getSrcPath(),
                    config.getTrustStore().getDestPath(),
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
                        .setConnectTimeout(Timeout.ofMilliseconds(config.getConnectTimeout()))
                        .setResponseTimeout(Timeout.ofMilliseconds(config.getResponseTimeout()))
                        .build();

        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setConnectionManager(connectionManagerBuilder.build())
                        .setDefaultRequestConfig(requestConfig)
                        .build();

        ClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    public static SSLContext createSSLContext1(String keyStorePath, String keyStorePassword)
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
        sslContext.init(keyManagerFactory.getKeyManagers(),  null, null);

        return sslContext;
    }

    public static SSLContext createSSLContext(String keyStorePath, String keyStorePassword) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, IOException, CertificateException {
        System.setProperty("jav.protocol.handler.pkgs", SSL_java_protocol_handler_pkgs);

        //Create KeyStore object
        KeyStore oKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream oFIS = new FileInputStream(keyStorePath);
        oKeyStore.load(oFIS, keyStorePassword.toCharArray());

        //Create KeyManagerFactory object
        KeyManagerFactory oKeyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        oKeyManagerFactory.init(oKeyStore , keyStorePassword.toCharArray());
        KeyManager[] km = oKeyManagerFactory.getKeyManagers();

        // Create TrustManagerFactory
        TrustManagerFactory oTrustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        oTrustManagerFactory.init(oKeyStore);
        TrustManager[] tm = oTrustManagerFactory.getTrustManagers();

        // Initialize moSSLContext
        SSLContext sslContext = SSLContext.getInstance("TLSV1.2");
        sslContext.init(km,  tm, null);
        return  sslContext;
    }


    public static void createTrustStore(
            Network network,
            String trustStoreSourcePath,
            String truststoreDestPath,
            String trustStorePassword)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        //        FileInputStream truststoreFile = new FileInputStream(truststoreDestPath);
        //        KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
        //        truststore.load(truststoreFile, trustStorePassword.toCharArray());
        //
        //        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //        try (InputStream caCertFile = new FileInputStream(trustStoreSourcePath)) {
        //            X509Certificate caCert = (X509Certificate) cf.generateCertificate(caCertFile);
        //            truststore.setCertificateEntry(
        //                    network.getName() + "_DSCert", caCert); // Provide an alias for the
        // certificate
        //            // Save the updated truststore
        //            try (FileOutputStream truststoreOutputStream =
        //                    new FileOutputStream(truststoreDestPath)) {
        //                truststore.store(truststoreOutputStream,
        // trustStorePassword.toCharArray());
        //            }
        //        }
    }
}
