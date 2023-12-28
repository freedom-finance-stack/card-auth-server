package org.freedomfinancestack.razorpay.cas.acs.controller;

import org.freedomfinancestack.razorpay.cas.acs.exception.ApiExceptionHandler;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationRequestService;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData.createSampleAREQ;
import static org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData.createSampleARES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationRequestControllerTest {

    @Mock private AuthenticationRequestService authenticationRequestService;

    @InjectMocks private AuthenticationRequestController authenticationRequestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(authenticationRequestController)
                        .setControllerAdvice(new ApiExceptionHandler())
                        .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void handleAuthenticationRequest_Success() throws Exception {
        // Arrange
        AREQ sampleAREQ = createSampleAREQ();
        ARES mockARES = createSampleARES();

        when(authenticationRequestService.processAuthenticationRequest(any(AREQ.class)))
                .thenReturn(mockARES);

        // Act & Assert
        mockMvc.perform(
                        post("/v2/transaction/authentication")
                                .content(objectMapper.writeValueAsString(sampleAREQ))
                                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void handleAuthenticationRequest_ThreeDSException() throws Exception {
        // Arrange
        AREQ sampleAREQ = createSampleAREQ();

        when(authenticationRequestService.processAuthenticationRequest(any(AREQ.class)))
                .thenThrow(
                        new ThreeDSException(
                                ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                                InternalErrorCode.INTERNAL_SERVER_ERROR));

        // Act & Assert
        mockMvc.perform(
                        post("/v2/transaction/authentication")
                                .content(objectMapper.writeValueAsString(sampleAREQ))
                                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andExpect(status().isOk());
    }
}
