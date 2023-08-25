package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CREQ extends ThreeDSObject {

    @JsonProperty("threeDSServerTransID")
    private String threeDSServerTransID;

    @JsonProperty("threeDSRequestorAppURL")
    private String threeDSRequestorAppURL;

    @JsonProperty("acsTransID")
    private String acsTransID;

    @JsonProperty("challengeWindowSize")
    private String challengeWindowSize;

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("messageVersion")
    private String messageVersion;

    @JsonProperty("sdkCounterStoA")
    private String sdkCounterStoA;

    @JsonProperty("sdkTransID")
    private String sdkTransID;

    // All Optional Fields - No optional fields available

    // All Conditional Fields
    @JsonProperty("challengeCancel")
    private String challengeCancel;

    @JsonProperty("challengeDataEntry")
    private String challengeDataEntry;

    @JsonProperty("challengeHTMLDataEntry")
    private String challengeHTMLDataEntry;

    @JsonProperty("messageExtension")
    private List<MessageExtension> messageExtension;

    @JsonProperty("resendChallenge")
    private String resendChallenge;

    @JsonProperty("challengeNoEntry")
    private String challengeNoEntry;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.CReq;
    }
}
