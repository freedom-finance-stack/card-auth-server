package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.util.EnumMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.mock.DsGatewayServiceMock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "gateway.ds")
@Getter
@Setter
@RequiredArgsConstructor
public class DsGatewayConfig {

    private final ApplicationContext applicationContext;
    private boolean mock;
    private Map<ClientType, ServiceConfig> services = new EnumMap<>(ClientType.class);

    @Getter
    @Setter
    public static class ServiceConfig {
        private String url;
        private boolean useSSL = false;
        private Integer connectTimeout = 3000;
        private Integer readTimeout = 3000;
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
        private int maxAttempts = 2;
    }

    @Bean("gatewayService")
    @ConditionalOnProperty(name = "gateway.ds.mock", havingValue = "true")
    public DsGatewayService dsGatewayServiceMock() {
        return new DsGatewayServiceMock();
    }

    @Bean("gatewayService")
    @ConditionalOnProperty(name = "gateway.ds.mock", havingValue = "false")
    public DsGatewayService dsGatewayService() {
        return applicationContext.getBean(DsGatewayServiceImpl.class);
    }
}
