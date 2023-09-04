package org.freedomfinancestack.razorpay.cas.acs.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VisaDsHttpsGatewayService extends HttpsGatewayService {

    private final RestTemplate visaDsRestTemplate;
    private final GatewayConfig.ServiceConfig serviceConfig;

    @Autowired
    public VisaDsHttpsGatewayService(
            @Qualifier("visaDsRestTemplate") RestTemplate visaDsRestTemplate,
            GatewayConfig gatewayConfig) {
        this.visaDsRestTemplate = visaDsRestTemplate;
        this.serviceConfig = gatewayConfig.getServices().get(ClientType.VISA_DS);
    }

    @Override
    RestTemplate getRestTemplate() {
        return this.visaDsRestTemplate;
    }

    @Override
    RetryTemplate getRetryTemplate() {
        return new CustomRetryTemplateBuilder()
                .withRetryMaxAttempts(serviceConfig.getRetryable().getMaxAttempts())
                .withHttpStatus(HttpStatus.TOO_MANY_REQUESTS)
                .withHttpStatus(HttpStatus.BAD_GATEWAY)
                .withHttpStatus(HttpStatus.GATEWAY_TIMEOUT)
                .withHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }

    @Override
    GatewayConfig.ServiceConfig getServiceConfig() {
        return this.serviceConfig;
    }
}
