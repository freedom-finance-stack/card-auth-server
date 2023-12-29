package org.freedomfinancestack.razorpay.cas.acs.gateway.threedsrequestor;

import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ThreedsRequestorCResService extends HttpsGatewayService {
    private final RestTemplate threedsRequestorRestTemplate;
    private final GatewayConfig.ServiceConfig serviceConfig;
    private final CResMapper cResMapper = Mappers.getMapper(CResMapper.class);

    @Autowired
    public ThreedsRequestorCResService(
            @Qualifier("threedsRequestorRestTemplate") RestTemplate threedsRequestorRestTemplate,
            GatewayConfig gatewayConfig) {
        this.threedsRequestorRestTemplate = threedsRequestorRestTemplate;
        this.serviceConfig =
                new GatewayConfig.ServiceConfig(
                        gatewayConfig.getServices().get(ClientType.THREEDS_REQUESTOR_SERVER));
    }

    public void sendNotificationCRes(Transaction transaction) throws ACSValidationException {
        if (serviceConfig.isMock()) {
            log.info("Mocking CRES");
            return;
        }

        this.serviceConfig.setUrl(transaction.getTransactionReferenceDetail().getNotificationUrl());

        CRES cres = cResMapper.toCres(transaction);
        cres.setAcsCounterAtoS(InternalConstants.INITIAL_ACS_SDK_COUNTER);
        try {
            log.info("Sending CRes: " + Util.toJson(cres));
            log.info("Base64Url Encoded CRes: " + Util.encodeBase64Url(cres));
            Map<String, String> headerMap = new HashMap<>();
            Map<String, Object> queryParamMap = new HashMap<>();
            MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
            requestMap.add("cres", Util.encodeBase64Url(cres));
            headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            String response = sendRequest(requestMap, HttpMethod.POST, headerMap, queryParamMap);
            log.info("CRes Received Successfully: " + Util.toJson(response));

        } catch (Exception exception) {
            log.error("Error while sending CRes", exception);
        }
    }

    @Override
    public RestTemplate getRestTemplate() {
        return this.threedsRequestorRestTemplate;
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
