package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
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
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            // Load the public key from PEM and convert it to X.509 certificate
            X509Certificate cert = loadX509CertificateFromPEM(config.getKeyStore().getPath());
            // Add the certificate to the keystore using your key identifier as the alias
            keyStore.setCertificateEntry(config.getKeyStore().getIdentifier(), cert);

            SSLContext sslContext =
                    new SSLContextBuilder().loadTrustMaterial(keyStore, null).build();

            SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
            httpClient.setSSLSocketFactory(sslConFactory);
        }
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient.build());
        requestFactory.setConnectTimeout(config.getConnectTimeout());
        requestFactory.setReadTimeout(config.getReadTimeout());
        return new RestTemplate(requestFactory);
    }

    private X509Certificate loadX509CertificateFromPEM(String pemFilePath)
            throws CertificateException, IOException {
        // Read the content of the PEM file
        try (FileReader fileReader = new FileReader(pemFilePath);
                PEMParser pemParser = new PEMParser(fileReader)) {
            X509CertificateHolder certHolder = (X509CertificateHolder) pemParser.readObject();
            return new JcaX509CertificateConverter()
                    .setProvider(new BouncyCastleProvider())
                    .getCertificate(certHolder);
        }
    }
}
