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
public class MasterCardDsHttpsGatewayService extends HttpsGatewayService {

    private final RestTemplate masterCardDsRestTemplate;
    private final DsGatewayConfig.ServiceConfig serviceConfig;

    @Autowired
    public MasterCardDsHttpsGatewayService(
            @Qualifier("masterCardDsRestTemplate") RestTemplate masterCardDsRestTemplate,
            DsGatewayConfig dsGatewayConfig) {
        this.masterCardDsRestTemplate = masterCardDsRestTemplate;
        this.serviceConfig = dsGatewayConfig.getServices().get(ClientType.MASTERCARD_DS);
    }

    @Override
    public RestTemplate getRestTemplate() {
        return this.masterCardDsRestTemplate;
    }

    @Override
    public RetryTemplate getRetryTemplate() {
        return new CustomRetryTemplateBuilder()
                .withRetryMaxAttempts(serviceConfig.getRetryable().getMaxAttempts())
                .withBackOffPeriod(serviceConfig.getRetryable().getBackOffPeriod())
                .withHttpStatus(HttpStatus.BAD_GATEWAY)
                .withHttpStatus(HttpStatus.GATEWAY_TIMEOUT)
                .withHttpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }

    @Override
    public DsGatewayConfig.ServiceConfig getServiceConfig() {
        return this.serviceConfig;
    }
}
