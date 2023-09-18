package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.config.DsGatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

public abstract class HttpsGatewayService {

    public abstract RestTemplate getRestTemplate();

    public abstract RetryTemplate getRetryTemplate();

    public abstract DsGatewayConfig.ServiceConfig getServiceConfig();

    public String sendRequest(
            String requestBody,
            HttpMethod method,
            Map<String, String> headerMap,
            Map<String, Object> queryParam) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAll(headerMap);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity =
                this.getRestTemplate()
                        .exchange(
                                this.getServiceConfig().getUrl(),
                                method,
                                entity,
                                String.class,
                                queryParam);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new GatewayHttpStatusCodeException(
                    responseEntity.getStatusCode(), responseEntity.getBody());
        }
    }

    public String sendRequestWithRetry(
            String requestBody,
            HttpMethod method,
            Map<String, String> headerMap,
            Map<String, Object> queryParam)
            throws GatewayHttpStatusCodeException {
        try {
            return this.getRetryTemplate()
                    .execute(context -> sendRequest(requestBody, method, headerMap, queryParam));
        } catch (RuntimeException e) {
            // The original exception that occurred during the last retry is available as
            // e.getCause()
            Throwable cause = e.getCause();
            if (cause instanceof GatewayHttpStatusCodeException) {
                throw (GatewayHttpStatusCodeException) cause;
            }
            throw e;
        }
    }
}
