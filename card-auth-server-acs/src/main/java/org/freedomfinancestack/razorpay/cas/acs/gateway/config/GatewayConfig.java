package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.*;

@Configuration
@ConfigurationProperties(prefix = "gateway")
@Getter
@Setter
public class GatewayConfig {
    private Map<ClientType, ServiceConfig> services = new EnumMap<>(ClientType.class);

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ServiceConfig {
        private boolean mock;
        private String url;
        private boolean useSSL = false;
        private Integer connectTimeout = 3000;
        private Integer responseTimeout = 3000;
        private KeyStoreConfig keyStore;
        private TrustStoreConfig trustStore;
        private RetryConfig retryable;

        public ServiceConfig(ServiceConfig serviceConfig) {
            this.mock = serviceConfig.isMock();
            this.url = serviceConfig.getUrl();
            this.useSSL = serviceConfig.isUseSSL();
            this.connectTimeout = serviceConfig.getConnectTimeout();
            this.responseTimeout = serviceConfig.getResponseTimeout();
            this.keyStore = new KeyStoreConfig(serviceConfig.getKeyStore());
            this.trustStore = new TrustStoreConfig(serviceConfig.getTrustStore());
            this.retryable = new RetryConfig(serviceConfig.getRetryable());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class KeyStoreConfig {
        private String path;
        private String password;

        public KeyStoreConfig(KeyStoreConfig keyStore) {
            this.path = keyStore.getPath();
            this.password = keyStore.getPassword();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TrustStoreConfig {
        private String path;
        private String password;

        public TrustStoreConfig(TrustStoreConfig trustStore) {
            this.path = trustStore.getPath();
            this.password = trustStore.getPassword();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RetryConfig {
        private int maxAttempts = 2;
        private Long backOffPeriod;

        public RetryConfig(RetryConfig retryable) {
            this.maxAttempts = retryable.getMaxAttempts();
            this.backOffPeriod = retryable.getBackOffPeriod();
        }
    }
}
