package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.ProprietaryULTest.PlrqService;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.OtpCommunicationConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.freedomfinancestack.razorpay.cas.acs.service.OtpService;
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

        //        if (!Util.isNullorBlank(emailId)) {
        //            Map<String, String> dataMap =
        //                    new HashMap<>() {
        //                        {
        //                            put("otp", otp);
        //                        }
        //                    };
        //            EmailNotificationDto emailMessage =
        //                    EmailNotificationDto.builder()
        //                            .to(Collections.singletonList(emailId))
        //                            .from(otpCommunicationConfiguration.getEmail().getFrom())
        //
        // .subject(otpCommunicationConfiguration.getEmail().getSubjectText())
        //                            .templateData(dataMap)
        //                            .templateName(
        //
        // otpCommunicationConfiguration.getEmail().getTemplateName())
        //                            .build();
        //            emailSent = notificationService.send(NotificationChannelType.EMAIL,
        // emailMessage);
        //            if (emailSent) {
        //                log.info("Email Sent successfully");
        //            } else {
        //                log.error("Unable to send Email!!!");
        //            }
        //        }
        //
        //        if (!Util.isNullorBlank(mobileNumber)) {
        //            SMSNotificationDto smsMessage =
        //                    SMSNotificationDto.builder()
        //                            .message(
        //                                    String.format(
        //
        // otpCommunicationConfiguration.getSms().getContent(),
        //                                            otp))
        //                            .to(Collections.singletonList(mobileNumber))
        //                            .priority(0)
        //                            .build();
        //            smsSent = notificationService.send(NotificationChannelType.SMS, smsMessage);
        //            if (smsSent) {
        //                log.info("SMS Sent successfully");
        //            } else {
        //                log.error("Unable to send SMS!!!");
        //            }
        //        }
        //
        //        if (!emailSent && !smsSent) {
        //            log.error("Unable to send SMS and email!!! TransactionId: {}",
        // transaction.getId());
        //            // throw exception
        //            transaction.setErrorCode(
        //                    InternalErrorCode.TRANSACTION_TIMEOUT_OTP_SEND_ERROR.getCode());
        //        }

        plrqService.sendPlrq(transaction.getId(), otp, transaction.getMessageVersion());
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
