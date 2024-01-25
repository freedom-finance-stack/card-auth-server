package org.freedomfinancestack.razorpay.cas.acs.gateway.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomRetryTemplateBuilderTest {

    @Test
    void withHttpStatus() {
        CustomRetryTemplateBuilder customRetryTemplateBuilder = new CustomRetryTemplateBuilder();
        CustomRetryTemplateBuilder customRetryTemplateBuilderActual =
                customRetryTemplateBuilder.withHttpStatus(HttpStatus.OK);
        assertNotNull(customRetryTemplateBuilderActual);
    }

    @Test
    void withRetryMaxAttempts() {
        CustomRetryTemplateBuilder customRetryTemplateBuilder = new CustomRetryTemplateBuilder();
        CustomRetryTemplateBuilder customRetryTemplateBuilderActual =
                customRetryTemplateBuilder.withRetryMaxAttempts(3);
        assertNotNull(customRetryTemplateBuilderActual);
    }

    @Test
    void withBackOffPeriod() {
        CustomRetryTemplateBuilder customRetryTemplateBuilder = new CustomRetryTemplateBuilder();
        CustomRetryTemplateBuilder customRetryTemplateBuilderActual =
                customRetryTemplateBuilder.withBackOffPeriod(3);
        assertNotNull(customRetryTemplateBuilderActual);
    }

    @Test
    void build_httpStatusRetry_IsEmpty() {
        CustomRetryTemplateBuilder customRetryTemplateBuilder = new CustomRetryTemplateBuilder();
        RetryTemplate retryTemplate = customRetryTemplateBuilder.build();
        assertNotNull(retryTemplate);
    }
}
