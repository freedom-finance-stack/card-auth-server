package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayConfig {
    private Map<ClientType, ServiceConfig> services = new EnumMap<>(ClientType.class);

    public Map<ClientType, ServiceConfig> getServices() {
        return services;
    }

    public void setServices(Map<ClientType, ServiceConfig> services) {
        this.services = services;
    }

    @Getter
    @Setter
    public static class ServiceConfig {
        private boolean mock;
        private String url;
        private KeyStoreConfig keyStore;
        private RetryConfig retryable;
    }

    @Getter
    @Setter
    public static class KeyStoreConfig {
        private Resource path;
        private String password;
    }

    @Getter
    @Setter
    public static class RetryConfig {
        private int maxAttempts;
    }
}
