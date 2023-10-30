package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.time.LocalDateTime;

import org.freedomfinancestack.razorpay.cas.acs.service.OtpService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.dao.enums.OtpVerificationStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpTransactionDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.OtpTransactionDetailRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("OtpService")
@Slf4j
@RequiredArgsConstructor
// Todo Implement encryption for storing otp values
public class OtpServiceImpl implements OtpService {

    private final OtpTransactionDetailRepository otpTransactionDetailRepository;
    private final RandomNumberGenerator randomNumberGenerator;
    private static final Integer EXPIRE_MIN = 10; // todo fetch from config

    @Override
    public OtpTransactionDetail generateOTP(String transactionId, OtpConfig otpConfig) {
        boolean generateNew = false;
        OtpTransactionDetail otpTransactionDetail =
                otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED);
        if (otpTransactionDetail == null) {
            generateNew = true;
        } else {
            LocalDateTime createdAt = otpTransactionDetail.getCreatedAt().toLocalDateTime();
            if (isExpired(createdAt)) {
                otpTransactionDetail.setVerificationStatus(OtpVerificationStatus.EXPIRED);
                otpTransactionDetailRepository.save(otpTransactionDetail);
                generateNew = true;
            }
        }
        if (generateNew) {
            log.info("Generating OTP for transactionId: " + transactionId);
            String otp = randomNumberGenerator.getIntRandomNumberInRange(otpConfig.getLength());
            otpTransactionDetail =
                    OtpTransactionDetail.builder()
                            .id(Util.generateUUID())
                            .transactionId(transactionId)
                            .value(otp)
                            .verificationStatus(OtpVerificationStatus.CREATED)
                            .build();
            otpTransactionDetailRepository.save(otpTransactionDetail);
        }

        return otpTransactionDetail;
    }

    @Override
    public boolean validateOTP(String transactionId, String otp) {
        OtpTransactionDetail otpTransactionDetail =
                otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED);
        if (otpTransactionDetail == null
                || Util.isNullorBlank(otpTransactionDetail.getValue())
                || OtpVerificationStatus.EXPIRED.equals(
                        otpTransactionDetail.getVerificationStatus())) {
            return false;
        }
        LocalDateTime createdAt = otpTransactionDetail.getCreatedAt().toLocalDateTime();
        if (isExpired(createdAt)) {
            otpTransactionDetail.setVerificationStatus(OtpVerificationStatus.EXPIRED);
            otpTransactionDetailRepository.save(otpTransactionDetail);
            return false;
        }
        if (otpTransactionDetail.getValue().equals(otp)) {
            otpTransactionDetail.setVerificationStatus(OtpVerificationStatus.VERIFIED);
            otpTransactionDetailRepository.save(otpTransactionDetail);
            return true;
        }
        return false;
    }

    public boolean isExpired(LocalDateTime createdAt) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        long minutes = createdAt.until(currentDateTime, java.time.temporal.ChronoUnit.MINUTES);
        if (minutes > EXPIRE_MIN) {
            return true;
        }
        return false;
    }
}
