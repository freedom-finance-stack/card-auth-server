package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "gateway")
@Getter
@Setter
@RequiredArgsConstructor
public class GatewayConfig {
    private final ApplicationContext applicationContext;
    private Map<ClientType, ServiceConfig> services = new EnumMap<>(ClientType.class);

    @Getter
    @Setter
    public static class ServiceConfig {
        private boolean mock;
        private String url;
        private boolean useSSL = false;
        private Integer connectTimeout = 3000;
        private Integer responseTimeout = 3000;
        private KeyStoreConfig keyStore;
        private TrustStoreConfig trustStore;
        private RetryConfig retryable;
    }

    @Getter
    @Setter
    public static class KeyStoreConfig {
        private String path;
        private String password;
    }

    @Getter
    @Setter
    public static class TrustStoreConfig {
        private String path;
        private String password;
    }

    @Getter
    @Setter
    public static class RetryConfig {
        private int maxAttempts = 2;
        private Long backOffPeriod;
    }
}
