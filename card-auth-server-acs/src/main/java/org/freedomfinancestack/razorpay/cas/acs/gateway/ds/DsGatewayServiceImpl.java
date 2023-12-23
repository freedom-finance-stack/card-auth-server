package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ContentType;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.gateway.mock.DsGatewayServiceMock;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.google.gson.JsonSyntaxException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("gatewayService")
@RequiredArgsConstructor
@Slf4j
@Primary
public class DsGatewayServiceImpl implements DsGatewayService {

    private final VisaDsHttpsGatewayService visaDsHttpsGatewayService;
    private final MasterCardDsHttpsGatewayService masterCardDsHttpsGatewayService;
    private final DsGatewayServiceMock dsGatewayServiceMock;

    public RRES sendRReq(final Network network, final RREQ rReq) throws ACSValidationException {
        HttpsGatewayService httpsGatewayService = getHttpsGatewayService(network);
        if (httpsGatewayService
                .getServiceConfig()
                .isMock()) { // todo dynemic injections on mock bean
            return dsGatewayServiceMock.sendRReq(network, rReq);
        }
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> queryParamMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        RRES rres = null;
        try {
            String strRres =
                    httpsGatewayService.sendRequestWithRetry(
                            Util.toJson(rReq), HttpMethod.POST, headerMap, queryParamMap);
            log.info("Result response received from DS{}", strRres);
            rres = Util.fromJson(strRres, RRES.class);
            log.info("Result response received from DS{}", rres);
        } catch (JsonSyntaxException jsonSyntaxException) {
            throw new ACSValidationException(
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    "invalid RRES received from DS");
        }
        return rres;
    }

    public void sendError(final Network network, final ThreeDSErrorResponse errorResponse)
            throws ACSValidationException {
        HttpsGatewayService httpsGatewayService = getHttpsGatewayService(network);
        if (httpsGatewayService.getServiceConfig().isMock()) {
            dsGatewayServiceMock.sendError(network, errorResponse);
            return;
        }
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> queryParamMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        try {
            // in case of Result Request flow fails, then only we are sending error message to DS.
            // As retry would be already done while sending RREQ we don't need, retry again in
            // sending error message
            httpsGatewayService.sendRequest(
                    Util.toJson(errorResponse), HttpMethod.POST, headerMap, queryParamMap);
        } catch (JsonSyntaxException jsonSyntaxException) {
            throw new ACSValidationException(
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    "invalid RRES received from DS");
        }
    }

    public void sendCRes(final Network network, final CRES cres, final String url) {
        HttpsGatewayService httpsGatewayService = getHttpsGatewayService(network);
        if (httpsGatewayService
                .getServiceConfig()
                .isMock()) { // todo dynemic injections on mock bean
            dsGatewayServiceMock.sendCRes(network, cres, url);
            return;
        }
        log.info("CRESTEST_2: {}", cres);
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> queryParamMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headerMap);
        HttpEntity<String> entity = new HttpEntity<>(Util.encodeBase64Url(cres), headers);
        log.info("CRESTEST_3: {}", entity);
        ResponseEntity<String> responseEntity =
                httpsGatewayService
                        .getRestTemplate()
                        .exchange(url, HttpMethod.POST, entity, String.class, queryParamMap);
        log.info("CRESTEST_4: {}", responseEntity);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.info(
                    "Server response received {}, with http status code: {}",
                    responseEntity.getBody(),
                    responseEntity.getStatusCode());
        } else {
            log.error("Server Error received with status code: {}", responseEntity.getStatusCode());
        }
    }

    private HttpsGatewayService getHttpsGatewayService(Network network) {
        switch (network) {
            case VISA:
                return this.visaDsHttpsGatewayService;
            case MASTERCARD:
                return this.masterCardDsHttpsGatewayService;
            default:
                throw new IllegalArgumentException("Invalid network");
        }
    }
}
