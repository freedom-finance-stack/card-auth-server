package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.dao.model.OtpConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpTransactionDetail;

/**
 * The {@code OtpService} interface is responsible for generating and validating OTPs.
 *
 * <p>The {@code OtpService}
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 */
public interface OtpService {
    /**
     * generate otp for transactionId for given length
     *
     * @param transactionId
     * @return
     */
    OtpTransactionDetail generateOTP(String transactionId, OtpConfig otpConfig);

    /**
     * validate otp for transactionId if transactionId is not found then return false. if
     * transactionId is found and otp is not expired and is valid then return true. if transactionId
     * is found and otp is not expired and is not valid then return false. if transactionId is found
     * and otp is expired then return false.
     *
     * @param transactionId
     * @param otp
     * @return boolean value indicating whether the OTP is valid or not.
     */
    boolean validateOTP(String transactionId, String otp);
}
