package org.freedomfinancestack.razorpay.cas.acs.gateway;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

public abstract class HttpsGatewayService {

    abstract RestTemplate getRestTemplate();

    abstract RetryTemplate getRetryTemplate();

    abstract GatewayConfig.ServiceConfig getServiceConfig();

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

    public String sendRequestRetry(
            String requestBody,
            HttpMethod method,
            Map<String, String> headerMap,
            Map<String, Object> queryParam) {
        return this.getRetryTemplate()
                .execute(
                        retryContext ->
                                this.sendRequest(requestBody, method, headerMap, queryParam));
    }
}
