package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ContentType;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class POrqService extends HttpsGatewayService {
    public static final String PORQ_MESSAGE_TYPE = "pOrq";
    public static final String PORQ_MESSAGE_VERSION = "1.0.6";
    public static final String PLRQ_URL_PARAM = "info";
    public static final String PORQ_URL_PARAM = "oob";

    private final RestTemplate ulRestTemplate;
    private final GatewayConfig.ServiceConfig serviceConfig;

    @Autowired
    public POrqService(
            @Qualifier("ulTestRestTemplate") RestTemplate visaDsRestTemplate,
            GatewayConfig gatewayConfig) {
        this.ulRestTemplate = visaDsRestTemplate;
        this.serviceConfig =
                new GatewayConfig.ServiceConfig(
                        gatewayConfig.getServices().get(ClientType.UL_TEST_PORTAL));
        this.serviceConfig.setUrl(
                this.serviceConfig.getUrl().replace(PLRQ_URL_PARAM, PORQ_URL_PARAM));
    }

    public POrs sendPOrq(String transactionId, String threeDSServerTransID, String messageVersion)
            throws ACSValidationException {
        if (serviceConfig.isMock()) {
            log.info("Mocking PORQ");
            POrs pors = new POrs();
            pors.setP_isOobSuccessful(true);
            return pors;
        }
        POrq pOrq =
                new POrq(
                        PORQ_MESSAGE_TYPE,
                        transactionId,
                        threeDSServerTransID,
                        messageVersion,
                        PORQ_MESSAGE_VERSION);
        try {
            log.info("Sending pOrq: " + Util.toJson(pOrq));
            Map<String, String> headerMap = new HashMap<>();
            Map<String, Object> queryParamMap = new HashMap<>();
            headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            String strPOrs =
                    sendRequest(Util.toJson(pOrq), HttpMethod.POST, headerMap, queryParamMap);
            POrs pOrs = Util.fromJson(strPOrs, POrs.class);
            log.info("pOrs: " + strPOrs);
            return pOrs;
        } catch (Exception exception) {
            log.error("Error while sending PLRQ", exception);
        }
        POrs pors = new POrs();
        pors.setP_isOobSuccessful(false);
        return pors;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return this.ulRestTemplate;
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
    public GatewayConfig.ServiceConfig getServiceConfig() {
        return this.serviceConfig;
    }
}
