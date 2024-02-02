package org.freedomfinancestack.razorpay.cas.acs.gateway.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class GatewayHttpStatusCodeExceptionTest {

    @Test
    public void
            test_GatewayHttpStatusCodeExceptionTest_constructor_initializes_httpStatus_and_body() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.OK;
        String body = "Test body";

        // Act
        GatewayHttpStatusCodeException exception =
                new GatewayHttpStatusCodeException(httpStatus, body);

        // Assert
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(body, exception.getBody());
    }

    @Test
    public void test_GatewayHttpStatusCodeExceptionTest_inherits_from_RuntimeException() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.OK;
        String body = "Test body";

        // Act
        GatewayHttpStatusCodeException exception =
                new GatewayHttpStatusCodeException(httpStatus, body);

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    public void test_body_is_empty_string() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.OK;
        String body = "";

        // Act
        GatewayHttpStatusCodeException exception =
                new GatewayHttpStatusCodeException(httpStatus, body);

        // Assert
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(body, exception.getBody());
    }

    @Test
    public void test_GatewayHttpStatusCodeExceptionTest_httpStatus_is_custom_status_code() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.valueOf(400);
        String body = "Test body";

        // Act
        GatewayHttpStatusCodeException exception =
                new GatewayHttpStatusCodeException(httpStatus, body);

        // Assert
        assertEquals(httpStatus, exception.getHttpStatus());
        assertEquals(body, exception.getBody());
    }
}
