package org.freedomfinancestack.razorpay.cas.acs.gateway.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class GatewayHttpStatusCodeException extends RuntimeException {

    private static final long serialVersionUID = 5696801857651587810L;
    private final HttpStatus statusCode;
    private final String body;

    public GatewayHttpStatusCodeException(HttpStatus statusCode, String body) {
        super(body);
        this.statusCode = statusCode;
        this.body = body;
    }
}
