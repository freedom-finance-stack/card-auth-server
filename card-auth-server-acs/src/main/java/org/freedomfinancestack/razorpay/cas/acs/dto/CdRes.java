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
    String acsTransID;
    String institutionName;
    String validationUrl;
    String schemaName;
    boolean jsEnableIndicator;
    String challengeText;
    String challengeInfoText;
    String resendBlocked;
    String attemptLeft;
    String messageVersion;
    String threeDSServerTransID;
    boolean challengeCompleted;

    // optional fields only used in case of error
    boolean isError;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.CDRes;
    }
}
