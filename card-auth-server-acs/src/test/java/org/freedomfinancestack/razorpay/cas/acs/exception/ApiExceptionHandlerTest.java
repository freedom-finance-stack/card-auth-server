// package org.freedomfinancestack.razorpay.cas.acs.exception;
//
// import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
// import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
// import org.junit.jupiter.api.Test;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.mock.web.MockHttpServletRequest;
// import org.springframework.mock.web.MockHttpServletResponse;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
//
// class ApiExceptionHandlerTest {
//
//    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();
//
//    @Test
//    void handleHttpRequestMethodNotSupported() {
//        // Arrange
//        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException(
//                HttpMethod.PUT.name(),
//                HttpMethod.GET.name(),
//                HttpMethod.POST.name()
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        // Act
//        apiExceptionHandler.handleHttpRequestMethodNotSupported(ex, headers,
// HttpStatus.BAD_REQUEST, request);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
//        // Add more assertions based on your specific logic and error response structure
//    }
// }
