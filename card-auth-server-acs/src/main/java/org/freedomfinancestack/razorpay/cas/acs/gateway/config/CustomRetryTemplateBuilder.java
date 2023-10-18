package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import java.util.HashSet;
import java.util.Set;

import org.freedomfinancestack.razorpay.cas.acs.gateway.exception.GatewayHttpStatusCodeException;
import org.springframework.classify.Classifier;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class CustomRetryTemplateBuilder {

    private static final int DEFAULT_MAX_ATTEMPS = 2;
    private static final long DEFAULT_BACKOFF_PERIOD = 1000l;
    private final Set<HttpStatus> httpStatusRetry;

    private int retryMaxAttempts = DEFAULT_MAX_ATTEMPS;

    private long backOffPeriod = DEFAULT_BACKOFF_PERIOD;

    public CustomRetryTemplateBuilder() {
        this.httpStatusRetry = new HashSet<>();
    }

    public CustomRetryTemplateBuilder withHttpStatus(HttpStatus httpStatus) {
        this.httpStatusRetry.add(httpStatus);
        return this;
    }

    public CustomRetryTemplateBuilder withRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
        return this;
    }

    public CustomRetryTemplateBuilder withBackOffPeriod(long backOffPeriod) {
        this.backOffPeriod = backOffPeriod;
        return this;
    }

    public RetryTemplate build() {
        if (this.httpStatusRetry.isEmpty()) {
            this.httpStatusRetry.addAll(getDefaults());
        }
        return createRetryTemplate();
    }

    private RetryTemplate createRetryTemplate() {
        RetryTemplate retry = new RetryTemplate();
        ExceptionClassifierRetryPolicy policy = new ExceptionClassifierRetryPolicy();
        policy.setExceptionClassifier(configureStatusCodeBasedRetryPolicy());
        retry.setRetryPolicy(policy);

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(backOffPeriod);
        retry.setBackOffPolicy(fixedBackOffPolicy);

        return retry;
    }

    private Classifier<Throwable, RetryPolicy> configureStatusCodeBasedRetryPolicy() {
        // one execution + 3 retries
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(1 + this.retryMaxAttempts);

        NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();

        return throwable -> {
            if (throwable instanceof GatewayHttpStatusCodeException) {
                GatewayHttpStatusCodeException httpException =
                        (GatewayHttpStatusCodeException) throwable;
                return getRetryPolicyForStatus(
                        httpException.getHttpStatus(), simpleRetryPolicy, neverRetryPolicy);
            }
            return neverRetryPolicy;
        };
    }

    private RetryPolicy getRetryPolicyForStatus(
            HttpStatus httpStatusCode,
            SimpleRetryPolicy simpleRetryPolicy,
            NeverRetryPolicy neverRetryPolicy) {

        if (this.httpStatusRetry.contains(httpStatusCode)) {
            return simpleRetryPolicy;
        }
        return neverRetryPolicy;
    }

    private Set<HttpStatus> getDefaults() {
        return Set.of(
                HttpStatus.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                HttpStatus.valueOf(HttpStatus.BAD_GATEWAY.value()),
                HttpStatus.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()));
    }
}
