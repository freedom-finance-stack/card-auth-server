package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.config.GatewayConfig;
import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HttpsGatewayService {

    public abstract RestTemplate getRestTemplate();

    public abstract RetryTemplate getRetryTemplate();

    public abstract GatewayConfig.ServiceConfig getServiceConfig();

    // TODO: make request body as Object for NotificationCRESSERVICE
    public String sendRequest(
            String requestBody,
            HttpMethod method,
            Map<String, String> headerMap,
            Map<String, Object> queryParam) {

        HttpHeaders headers = new HttpHeaders();
        if (headerMap != null) {
            headers.setAll(headerMap);
        }
        if (queryParam == null) {
            queryParam = new HashMap<>();
        }
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
            log.info("Gateway response received {}", responseEntity.getBody());
            return responseEntity.getBody();
        } else {
            throw new GatewayHttpStatusCodeException(
                    HttpStatus.valueOf(responseEntity.getStatusCode().value()),
                    responseEntity.getBody());
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
            } else if (cause instanceof SocketTimeoutException) {
                throw new GatewayHttpStatusCodeException(
                        HttpStatus.GATEWAY_TIMEOUT, cause.getMessage());
            }
            throw e;
        }
    }
}
