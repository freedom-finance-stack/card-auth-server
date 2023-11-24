package org.freedomfinancestack.razorpay.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppOtpHtmlParams {
    String transactionId;
    String institutionName;
    String validationUrl;
    String schemaName;
    String challengeText;
    String challengeInfoText;
    String psImage;
    String issuerImage;
    String resendBlocked;
    String attemptLeft;
    String timeoutInMinutes;
    String timeoutInSeconds;
    String mobileNumber;
    String cardNumber;
    String merchantName;
    String transactionDate;
    String amountWithCurrency;
    boolean challengeCompleted;
}
