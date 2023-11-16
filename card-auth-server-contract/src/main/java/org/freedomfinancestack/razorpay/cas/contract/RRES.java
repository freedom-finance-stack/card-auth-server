package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RRES extends ThreeDSObject {
    // All Required Fields
    private String threeDSServerTransID;

    private String acsTransID;

    private String dsTransID;

    private String messageType;

    private String messageVersion;

    private String resultsStatus;

    private String additionalUnspecifiedField;

    // All Optional Fields

    // All Conditional Fields
    private List<MessageExtension> messageExtension;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.RRes;
    }
}
