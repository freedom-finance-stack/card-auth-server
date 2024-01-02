package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.freedomfinancestack.razorpay.cas.dao.enums.OtpVerificationStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.OtpTransactionDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.OtpTransactionDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @Mock private OtpTransactionDetailRepository otpTransactionDetailRepository;

    @Mock private RandomNumberGenerator randomNumberGenerator;

    @InjectMocks private OtpServiceImpl otpService;

    @Test
    void generateOTP_NewTransaction_ShouldGenerateNewOTP() {
        // Arrange
        String transactionId = "sampleTransactionId";
        OtpConfig otpConfig = new OtpConfig(6); // Assuming length and expire time
        OtpTransactionDetail existingOtpTransactionDetail = null;
        when(otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED))
                .thenReturn(existingOtpTransactionDetail);
        when(randomNumberGenerator.getIntRandomNumberInRange(otpConfig.getLength()))
                .thenReturn("123456");

        // Act
        OtpTransactionDetail result = otpService.generateOTP(transactionId, otpConfig);

        // Assert
        assertNotNull(result, "Generated OTP transaction detail should not be null");
        assertEquals(transactionId, result.getTransactionId(), "Transaction ID should match");
        assertEquals("123456", result.getValue(), "Generated OTP value should match");
        assertEquals(
                OtpVerificationStatus.CREATED,
                result.getVerificationStatus(),
                "Verification status should be CREATED");
        verify(otpTransactionDetailRepository, times(1)).save(result);
    }

    @Test
    void generateOTP_ExpiredTransaction_ShouldGenerateNewOTP() {
        // Arrange
        String transactionId = "sampleTransactionId";
        OtpConfig otpConfig = new OtpConfig(6); // Assuming length and expire time
        OtpTransactionDetail expiredOtpTransactionDetail = new OtpTransactionDetail();
        expiredOtpTransactionDetail.setCreatedAt(
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(11)));
        expiredOtpTransactionDetail.setVerificationStatus(OtpVerificationStatus.CREATED);
        when(otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED))
                .thenReturn(expiredOtpTransactionDetail);
        when(randomNumberGenerator.getIntRandomNumberInRange(otpConfig.getLength()))
                .thenReturn("123456");

        // Act
        OtpTransactionDetail result = otpService.generateOTP(transactionId, otpConfig);

        // Assert
        assertNotNull(result, "Generated OTP transaction detail should not be null");
        assertEquals(transactionId, result.getTransactionId(), "Transaction ID should match");
        assertEquals("123456", result.getValue(), "Generated OTP value should match");
        assertEquals(
                OtpVerificationStatus.CREATED,
                result.getVerificationStatus(),
                "Verification status should be CREATED");
        verify(otpTransactionDetailRepository, times(1)).save(result);
        assertEquals(
                OtpVerificationStatus.EXPIRED,
                expiredOtpTransactionDetail.getVerificationStatus(),
                "Expired OTP transaction detail should be updated to EXPIRED");
        verify(otpTransactionDetailRepository, times(1)).save(expiredOtpTransactionDetail);
    }

    @Test
    void validateOTP_ValidOTP_ShouldReturnTrue() {
        // Arrange
        String transactionId = "sampleTransactionId";
        String otp = "123456";
        OtpTransactionDetail validOtpTransactionDetail = new OtpTransactionDetail();
        validOtpTransactionDetail.setValue(otp);
        validOtpTransactionDetail.setCreatedAt(
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        validOtpTransactionDetail.setVerificationStatus(OtpVerificationStatus.CREATED);
        when(otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED))
                .thenReturn(validOtpTransactionDetail);

        // Act
        boolean result = otpService.validateOTP(transactionId, otp);

        // Assert
        assertTrue(result, "Validation of valid OTP should return true");
        assertEquals(
                OtpVerificationStatus.VERIFIED,
                validOtpTransactionDetail.getVerificationStatus(),
                "Valid OTP transaction detail should be updated to VERIFIED");
        verify(otpTransactionDetailRepository, times(1)).save(validOtpTransactionDetail);
    }

    @Test
    void validateOTP_ExpiredOTP_ShouldReturnFalse() {
        // Arrange
        String transactionId = "sampleTransactionId";
        String otp = "123456";
        OtpTransactionDetail expiredOtpTransactionDetail = new OtpTransactionDetail();
        expiredOtpTransactionDetail.setValue(otp);
        expiredOtpTransactionDetail.setCreatedAt(
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(11)));
        expiredOtpTransactionDetail.setVerificationStatus(OtpVerificationStatus.CREATED);
        when(otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED))
                .thenReturn(expiredOtpTransactionDetail);

        // Act
        boolean result = otpService.validateOTP(transactionId, otp);

        // Assert
        assertFalse(result, "Validation of expired OTP should return false");
        assertEquals(
                OtpVerificationStatus.EXPIRED,
                expiredOtpTransactionDetail.getVerificationStatus(),
                "Expired OTP transaction detail should be updated to EXPIRED");
        verify(otpTransactionDetailRepository, times(1)).save(expiredOtpTransactionDetail);
    }

    @Test
    void validateOTP_IncorrectOTP_ShouldReturnFalse() {
        // Arrange
        String transactionId = "sampleTransactionId";
        String otp = "123456";
        OtpTransactionDetail incorrectOtpTransactionDetail = new OtpTransactionDetail();
        incorrectOtpTransactionDetail.setValue("654321");
        incorrectOtpTransactionDetail.setCreatedAt(
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        incorrectOtpTransactionDetail.setVerificationStatus(OtpVerificationStatus.CREATED);
        when(otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED))
                .thenReturn(incorrectOtpTransactionDetail);

        // Act
        boolean result = otpService.validateOTP(transactionId, otp);

        // Assert
        assertFalse(result, "Validation of incorrect OTP should return false");
        assertEquals(
                OtpVerificationStatus.CREATED,
                incorrectOtpTransactionDetail.getVerificationStatus(),
                "Incorrect OTP transaction detail should remain in CREATED status");
        verify(otpTransactionDetailRepository, never()).save(incorrectOtpTransactionDetail);
    }

    // Additional tests for other scenarios (e.g., null transaction, null or blank OTP, etc.)

    @Test
    void isExpired_CurrentTimeWithinExpiryTime_ShouldReturnFalse() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(5);

        // Act
        boolean result = otpService.isExpired(createdAt);

        // Assert
        assertFalse(result, "OTP should not be expired");
    }

    @Test
    void isExpired_CurrentTimeOutsideExpiryTime_ShouldReturnTrue() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(15);

        // Act
        boolean result = otpService.isExpired(createdAt);

        // Assert
        assertTrue(result, "OTP should be expired");
    }

    @Test
    void validateOTP_NullTransactionId_ShouldReturnFalse() {
        // Arrange
        String transactionId = null;
        String otp = "123456";

        // Act
        boolean result = otpService.validateOTP(transactionId, otp);

        // Assert
        assertFalse(result, "Validating OTP with null transactionId should return false");
        verify(otpTransactionDetailRepository, never()).save(any());
    }

    @Test
    void validateOTP_NullOtp_ShouldReturnFalse() {
        // Arrange
        String transactionId = "sampleTransactionId";
        String otp = null;

        // Act
        boolean result = otpService.validateOTP(transactionId, otp);

        // Assert
        assertFalse(result, "Validating null OTP should return false");
        verify(otpTransactionDetailRepository, never()).save(any());
    }

    @Test
    void validateOTP_NullOtpTransactionDetail_ShouldReturnFalse() {
        // Arrange
        String transactionId = "sampleTransactionId";
        String otp = "123456";
        when(otpTransactionDetailRepository
                        .findTopByTransactionIdAndVerificationStatusOrderByCreatedAtDesc(
                                transactionId, OtpVerificationStatus.CREATED))
                .thenReturn(null);

        // Act
        boolean result = otpService.validateOTP(transactionId, otp);

        // Assert
        assertFalse(result, "Validating OTP with null transaction detail should return false");
        verify(otpTransactionDetailRepository, never()).save(any());
    }
}
