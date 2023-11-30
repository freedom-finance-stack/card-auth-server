package org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.hc.core5.http.ContentType;
import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ClientType;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.CustomRetryTemplateBuilder;
import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
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
public class PlrqService extends HttpsGatewayService {
    public static final String BRW_FORM_DATA = "transactionId=%s&authVal=%s";
    public static final String BRW_CANCEL_FORM_DATA = "transactionId=%s&cancelChallenge=true";
    public static final String PLRQ_MESSAGE_TYPE = "pIrq";
    public static final String PLRQ_MESSAGE_VERSION = "1.0.6";
    public static final String APP_INCORRECT_DATA = "000000";

    private final AppConfiguration appConfiguration;
    private final RestTemplate ulRestTemplate;
    private final GatewayConfig.ServiceConfig serviceConfig;

    @Autowired
    public PlrqService(
            AppConfiguration appConfiguration,
            @Qualifier("ulTestRestTemplate") RestTemplate ulTestRestTemplate,
            GatewayConfig gatewayConfig) {
        this.appConfiguration = appConfiguration;
        this.ulRestTemplate = ulTestRestTemplate;
        this.serviceConfig = gatewayConfig.getServices().get(ClientType.UL_TEST_PORTAL);
    }

    public void sendPlrq(
            String transactionId, String otpAuthVal, String messageVersion, String deviceChannel)
            throws ACSValidationException {
        if (serviceConfig.isMock()) {
            log.info("Mocking PLRQ");
            return;
        }

        Plrq plrq = createPlrq(transactionId, otpAuthVal, messageVersion, deviceChannel);
        try {
            log.info("Sending PLRQ: " + Util.toJson(plrq));
            Map<String, String> headerMap = new HashMap<>();
            Map<String, Object> queryParamMap = new HashMap<>();
            headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            String strPlrs =
                    sendRequest(Util.toJson(plrq), HttpMethod.POST, headerMap, queryParamMap);
            log.info("PLRS response: " + strPlrs);
        } catch (Exception exception) {
            log.error("Error while sending PLRQ", exception);
        }
    }

    private Plrq createPlrq(
            String transactionId, String authVal, String messageVersion, String deviceChannel) {
        Plrq plrq = new Plrq();
        plrq.acsTransID = transactionId;
        plrq.messageType = PLRQ_MESSAGE_TYPE;
        plrq.p_messageVersion = PLRQ_MESSAGE_VERSION;
        plrq.messageVersion = messageVersion;

        switch (Objects.requireNonNull(DeviceChannel.getDeviceChannel(deviceChannel))) {
            case APP -> plrq.p_formValues_APP = createPlrqApp(authVal);
            case BRW -> plrq.p_formValues_BRW = createPlrqBrw(transactionId, authVal);
            default -> {}
        }
        return plrq;
    }

    private Plrq.PFormValuesAPP createPlrqApp(String authVal) {
        Plrq.PFormValuesAPP pFormValuesAPP = new Plrq.PFormValuesAPP();
        pFormValuesAPP.correctChallengeData = authVal;
        pFormValuesAPP.incorrectChallengeData = APP_INCORRECT_DATA;
        return pFormValuesAPP;
    }

    private Plrq.PFormValuesBRW createPlrqBrw(String transactionId, String authVal) {
        Plrq.PFormValuesBRW pFormValuesBRW = new Plrq.PFormValuesBRW();
        pFormValuesBRW.cancelFormData = String.format(BRW_CANCEL_FORM_DATA, transactionId);
        pFormValuesBRW.correctFormData = String.format(BRW_FORM_DATA, transactionId, authVal);
        pFormValuesBRW.incorrectFormData = String.format(BRW_FORM_DATA, transactionId, "00000");
        pFormValuesBRW.action =
                RouteConstants.getAcsChallengeValidationUrl(
                        appConfiguration.getHostname(), DeviceChannel.BRW.getChannel());
        return pFormValuesBRW;
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
