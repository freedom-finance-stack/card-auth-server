package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.CRES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code ChallengeResponse} class represents a response object for creq.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResponse {
    String transactionId;
    String authType;
    int refreshCount;
    String merchantName;
    String merchantData;
    String termURL;
    String jsEnableIndicator;
    String resendBlocked;
    String institutionName;

    // optional fields only used in case of error
    boolean isError;

    String encryptedCRes;
    String encryptedErro;
    String notificationUrl;

}
