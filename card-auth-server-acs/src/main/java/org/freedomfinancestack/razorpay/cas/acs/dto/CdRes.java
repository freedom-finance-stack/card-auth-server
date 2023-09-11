package org.freedomfinancestack.razorpay.cas.acs.dto;

import java.io.Serializable;

import org.freedomfinancestack.razorpay.cas.contract.ThreeDSObject;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code ChallengeResponse} class represents a response object for creq.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CdRes extends ThreeDSObject implements Serializable {
    String transactionId;
    String institutionName;
    String schemaName;
    boolean jsEnableIndicator;
    String challengeText;
    String resendBlocked;
    String attemptLeft;
    boolean challengeCompleted;
    // optional fields only used in case of error
    boolean isError;

    String encryptedCRes;
    String encryptedErro;
    String notificationUrl;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.CDRes;
    }
}