package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
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
public class CResService extends HttpsGatewayService {
    private final RestTemplate ulRestTemplate;
    private final GatewayConfig.ServiceConfig serviceConfig;

    @Autowired
    public CResService(
            @Qualifier("ulTestRestTemplate") RestTemplate visaDsRestTemplate,
            GatewayConfig gatewayConfig) {
        this.ulRestTemplate = visaDsRestTemplate;
        this.serviceConfig =
                new GatewayConfig.ServiceConfig(
                        gatewayConfig.getServices().get(ClientType.UL_TEST_PORTAL));
        //        this.serviceConfig.setUrl(
        //                this.serviceConfig.getUrl().replace(PLRQ_URL_PARAM, PORQ_URL_PARAM));
    }

    public void sendCRes(Transaction transaction) throws ACSValidationException {
        if (serviceConfig.isMock()) {
            log.info("Mocking CRES");
            return;
        }

        this.serviceConfig.setUrl(transaction.getTransactionReferenceDetail().getNotificationUrl());
        CRES cres =
                CRES.builder()
                        .threeDSServerTransID(
                                transaction
                                        .getTransactionReferenceDetail()
                                        .getThreedsServerTransactionId())
                        .acsCounterAtoS(InternalConstants.INITIAL_ACS_SDK_COUNTER)
                        .acsTransID(transaction.getId())
                        .challengeCompletionInd(InternalConstants.NO)
                        .messageType(MessageType.CRes.toString())
                        .messageVersion(transaction.getMessageVersion())
                        .transStatus(transaction.getTransactionStatus().getStatus())
                        .build();
        try {
            log.info("Sending CRes: " + Util.toJson(cres));
            log.info("Base64Url Encoded CRes: " + Util.encodeBase64Url(cres));
            Map<String, String> headerMap = new HashMap<>();
            Map<String, Object> queryParamMap = new HashMap<>();
            headerMap.put(
                    HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
            //            queryParamMap.put("cres", Util.encodeBase64Url(cres));
            String requestBody =
                    URLEncoder.encode("cres", StandardCharsets.UTF_8)
                            + "="
                            + URLEncoder.encode(Util.encodeBase64Url(cres), StandardCharsets.UTF_8);
            String response = sendRequest(requestBody, HttpMethod.POST, headerMap, queryParamMap);
            log.info("CRes Received Successfully: " + Util.toJson(response));

        } catch (Exception exception) {
            log.error("Error while sending CRes", exception);
        }
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
