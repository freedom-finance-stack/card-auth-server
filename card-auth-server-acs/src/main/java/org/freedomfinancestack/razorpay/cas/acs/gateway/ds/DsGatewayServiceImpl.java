package org.freedomfinancestack.razorpay.cas.acs.gateway.ds;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ContentType;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.HttpsGatewayService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.JsonSyntaxException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DsGatewayServiceImpl implements DsGatewayService {

    private final VisaDsHttpsGatewayService visaDsHttpsGatewayService;
    private final MasterCardDsHttpsGatewayService masterCardDsHttpsGatewayService;

    public RRES sendRReq(final Network network, final RREQ rReq) throws ACSValidationException {
        HttpsGatewayService httpsGatewayService = getHttpsGatewayService(network);
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> queryParamMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        RRES rres = null;
        try {
            String strRres =
                    httpsGatewayService.sendRequestWithRetry(
                            Util.toJson(rReq), HttpMethod.POST, headerMap, queryParamMap);
            rres = Util.fromJson(strRres, RRES.class);
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
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> queryParamMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
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
