package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.notification.NotificationService;
import org.freedomfinancestack.extensions.notification.dto.NotificationResponseDto;
import org.freedomfinancestack.extensions.notification.exception.NotificationException;
import org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData;
import org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.NotificationSentException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.PlrqService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.OtpCommunicationConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.OtpService;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;
import org.freedomfinancestack.razorpay.cas.dao.enums.OtpVerificationStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpTransactionDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.CHALLENGE_CORRECT_OTP_TEXT;
import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.CHALLENGE_INCORRECT_OTP_TEXT;
import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigTestData.createAuthConfigDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OTPAuthenticationServiceTest {
    @Mock OtpService otpService;
    //            = mock(OtpService.class);
    @Mock OtpCommunicationConfiguration otpCommunicationConfiguration;
    @Mock private PlrqService plrqService;
    @Mock private NotificationService notificationService;

    @InjectMocks OTPAuthenticationService otpAuthenticationService;
    //     public OTPAuthenticationService otpAuthenticationService =
    //             new OTPAuthenticationService(otpService, otpCommunicationConfiguration,
    //                     plrqService, notificationService);

    AuthConfigDto authConfigDto =
            createAuthConfigDto(OOBType.UL_TEST, true, true, AuthType.OTP, AuthType.UNKNOWN);
    AuthenticationDto authenticationDto =
            AuthConfigTestData.createAuthenticationDto(
                    authConfigDto,
                    TransactionTestData.createSampleAppTransaction(),
                    String.valueOf(AuthType.OTP));

    /**
     * This test cover the case when OTP is Successfully Send or not send Also takes into
     * consideration about the Exception being thrown in case OTP is not send Successfully due to
     * some ACS_TECHNICAL_ERROR.
     *
     * @param success => if the OTP is sent or not
     */
    @ParameterizedTest
    @CsvSource({"true", "false"})
    void preAuthenticate(String success) throws ThreeDSException, NotificationException {

        OtpTransactionDetail otpTransactionDetail =
                new OtpTransactionDetail("1", "1209", OtpVerificationStatus.CREATED, "2000");
        when(otpService.generateOTP(any(), any())).thenReturn(otpTransactionDetail);
        when(otpCommunicationConfiguration.getEmail())
                .thenReturn(mock(OtpCommunicationConfiguration.EmailProperties.class));
        when(otpCommunicationConfiguration.getEmail().getFrom()).thenReturn("v@gmail.com");
        when(otpCommunicationConfiguration.getEmail().getSubjectText()).thenReturn("Dummy Subject");
        when(otpCommunicationConfiguration.getEmail().getTemplateName())
                .thenReturn("Dummy template");

        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();
        notificationResponseDto.setSuccess(Boolean.parseBoolean(success));
        when(notificationService.send(any(), any())).thenReturn(notificationResponseDto);
        when(otpCommunicationConfiguration.getSms())
                .thenReturn(mock(OtpCommunicationConfiguration.SmsProperties.class));
        when(otpCommunicationConfiguration.getSms().getTemplateName()).thenReturn("templateName");

        // ACT

        if (!Boolean.parseBoolean(success)) {
            NotificationSentException notificationSentException =
                    assertThrows(
                            NotificationSentException.class,
                            () -> otpAuthenticationService.preAuthenticate(authenticationDto));
            assertEquals(
                    "Couldn't communicate otp to user", notificationSentException.getMessage());
        } else {
            otpAuthenticationService.preAuthenticate(authenticationDto);

            // VERIFY
            verify(otpService, atLeastOnce()).generateOTP(any(), any());
            verify(otpCommunicationConfiguration, atLeastOnce()).getEmail();
            verify(plrqService, atLeastOnce()).sendPlrq(any(), any(), any(), any(), any());
            verify(otpCommunicationConfiguration, atLeastOnce()).getSms();
            verify(notificationService, atLeastOnce()).send(any(), any());
            verify(plrqService, atLeastOnce()).sendPlrq(any(), any(), any(), any(), any());
        }
    }

    /** Considers the test case where service is unable to send OTP due to some internal error. */
    @Test
    public void preAuthenticate_NotificationNotSendException()
            throws RuntimeException, NotificationException {
        OtpTransactionDetail otpTransactionDetail =
                new OtpTransactionDetail("1", "1209", OtpVerificationStatus.CREATED, "2000");
        when(otpService.generateOTP(any(), any())).thenReturn(otpTransactionDetail);
        when(otpCommunicationConfiguration.getEmail())
                .thenReturn(mock(OtpCommunicationConfiguration.EmailProperties.class));
        when(otpCommunicationConfiguration.getEmail().getFrom()).thenReturn("v@gmail.com");
        when(otpCommunicationConfiguration.getEmail().getSubjectText()).thenReturn("Dummy Subject");
        when(otpCommunicationConfiguration.getEmail().getTemplateName())
                .thenReturn("Dummy template");

        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();
        notificationResponseDto.setSuccess(true);
        when(notificationService.send(any(), any()))
                .thenThrow(new RuntimeException("Unable to send OTP due to some technical glitch"));

        RuntimeException notificationSentException =
                assertThrows(
                        RuntimeException.class,
                        () -> otpAuthenticationService.preAuthenticate(authenticationDto));
        assertEquals(
                "Unable to send OTP due to some technical glitch",
                notificationSentException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    void authenticate(String authenticated) throws ThreeDSException {
        AuthResponse authResponseExpected = new AuthResponse();
        authResponseExpected.setAuthenticated(Boolean.parseBoolean(authenticated));

        when(otpService.validateOTP(any(), any())).thenReturn(Boolean.parseBoolean(authenticated));
        AuthResponse authResponseActual = otpAuthenticationService.authenticate(authenticationDto);

        if (Boolean.parseBoolean(authenticated)) {
            assertEquals(authResponseActual.getDisplayMessage(), (CHALLENGE_CORRECT_OTP_TEXT));
        } else {
            assertEquals(authResponseActual.getDisplayMessage(), (CHALLENGE_INCORRECT_OTP_TEXT));
        }
    }
}
