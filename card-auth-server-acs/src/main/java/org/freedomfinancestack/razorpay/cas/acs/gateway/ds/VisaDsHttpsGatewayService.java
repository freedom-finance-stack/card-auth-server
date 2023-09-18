package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.DsGatewayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VisaDsHttpsGatewayService extends HttpsGatewayService {

    private final RestTemplate visaDsRestTemplate;
    private final DsGatewayConfig.ServiceConfig serviceConfig;

    @Autowired
    public VisaDsHttpsGatewayService(
            @Qualifier("visaDsRestTemplate") RestTemplate visaDsRestTemplate,
            DsGatewayConfig dsGatewayConfig) {
        this.visaDsRestTemplate = visaDsRestTemplate;
        this.serviceConfig = dsGatewayConfig.getServices().get(ClientType.VISA_DS);
    }

    @Override
    RestTemplate getRestTemplate() {
        return this.visaDsRestTemplate;
    }

    @Override
    RetryTemplate getRetryTemplate() {
        return new CustomRetryTemplateBuilder()
                .withRetryMaxAttempts(serviceConfig.getRetryable().getMaxAttempts())
                .withBackOffPeriod(serviceConfig.getRetryable().getBackOffPeriod())
                .withHttpStatus(HttpStatus.BAD_GATEWAY)
                .withHttpStatus(HttpStatus.GATEWAY_TIMEOUT)
                .withHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }

    @Override
    DsGatewayConfig.ServiceConfig getServiceConfig() {
        return this.serviceConfig;
    }
}
