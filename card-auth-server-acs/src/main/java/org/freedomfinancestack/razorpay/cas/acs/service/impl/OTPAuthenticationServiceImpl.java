package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.notification.NotificationService;
import org.freedomfinancestack.extensions.notification.dto.EmailNotificationDto;
import org.freedomfinancestack.extensions.notification.dto.NotificationResponseDto;
import org.freedomfinancestack.extensions.notification.dto.SMSNotificationDto;
import org.freedomfinancestack.extensions.notification.enums.NotificationChannelType;
import org.freedomfinancestack.extensions.notification.exception.NotificationException;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.NotificationSentException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.PlrqService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.OtpCommunicationConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.freedomfinancestack.razorpay.cas.acs.service.OtpService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpTransactionDetail;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.CHALLENGE_CORRECT_OTP_TEXT;
import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.CHALLENGE_INCORRECT_OTP_TEXT;

/**
 * The {@code OTPAuthenticationServiceImpl} class is an implementation of the {@link
 * AuthenticationService} interface. This service is responsible for authenticating the user using
 * the One Time Password (OTP) and generating the Authentication Response.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("OTPAuthenticationService")
@Slf4j
@RequiredArgsConstructor
public class OTPAuthenticationServiceImpl implements AuthenticationService {
    private final OtpService otpService;
    private final OtpCommunicationConfiguration otpCommunicationConfiguration;
    private final PlrqService plrqService;
    private final NotificationService notificationService;

    @Override
    public void preAuthenticate(AuthenticationDto authentication) throws ThreeDSException {
        Transaction transaction = authentication.getTransaction();
        OtpTransactionDetail otpTransactionDetail =
                otpService.generateOTP(
                        transaction.getId(), authentication.getAuthConfigDto().getOtpConfig());

        String otp = otpTransactionDetail.getValue();
        String emailId = transaction.getTransactionCardHolderDetail().getEmailId();
        String mobileNumber = transaction.getTransactionCardHolderDetail().getMobileNumber();
        boolean emailSent = false;
        boolean smsSent = false;

        // send notification of otp
        try {
            if (!Util.isNullorBlank(emailId)) {
                Map<String, String> dataMap =
                        new HashMap<>() {
                            {
                                put("otp", otp);
                            }
                        };
                EmailNotificationDto emailMessage =
                        EmailNotificationDto.builder()
                                .to(Collections.singletonList(emailId))
                                .from(otpCommunicationConfiguration.getEmail().getFrom())
                                .subject(otpCommunicationConfiguration.getEmail().getSubjectText())
                                .templateData(dataMap)
                                .templateName(
                                        otpCommunicationConfiguration.getEmail().getTemplateName())
                                .build();
                NotificationResponseDto emailResponse =
                        notificationService.send(NotificationChannelType.EMAIL, emailMessage);
                if (emailResponse.isSuccess()) {
                    emailSent = true;
                    log.info("Email Sent successfully");
                } else {
                    log.error("Unable to send Email!!!");
                }
            }

            if (!Util.isNullorBlank(mobileNumber)) {
                Map<String, String> dataMap =
                        new HashMap<>() {
                            {
                                put("otp", otp);
                            }
                        };
                SMSNotificationDto smsMessage =
                        SMSNotificationDto.builder()
                                .templateName(
                                        otpCommunicationConfiguration.getSms().getTemplateName())
                                .templateData(dataMap)
                                .to(Collections.singletonList(mobileNumber))
                                .priority(0)
                                .build();
                NotificationResponseDto smsResponse =
                        notificationService.send(NotificationChannelType.SMS, smsMessage);
                if (smsResponse.isSuccess()) {
                    smsSent = true;
                    log.info("SMS Sent successfully");
                } else {
                    log.error("Unable to send SMS!!!");
                }
            }
            plrqService.sendPlrq(
                    transaction.getId(),
                    otp,
                    transaction.getMessageVersion(),
                    transaction.getDeviceChannel());
        } catch (NotificationException e) {
            throw new NotificationSentException(
                    ThreeDSecureErrorCode.SYSTEM_CONNECTION_FAILURE,
                    InternalErrorCode.UNABLE_TO_SEND_OTP,
                    e);
        }

        if (!emailSent && !smsSent) {
            log.error("Unable to send SMS and email!!! TransactionId: {}", transaction.getId());
            throw new NotificationSentException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.UNABLE_TO_SEND_OTP,
                    "Couldn't communicate otp to user");
        }
    }

    @Override
    public AuthResponse authenticate(AuthenticationDto authentication) throws ThreeDSException {
        boolean authenticated =
                otpService.validateOTP(
                        authentication.getTransaction().getId(), authentication.getAuthValue());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(authenticated);

        if (authenticated) authResponse.setDisplayMessage(CHALLENGE_CORRECT_OTP_TEXT);
        else authResponse.setDisplayMessage(CHALLENGE_INCORRECT_OTP_TEXT);

        return authResponse;
    }
}
