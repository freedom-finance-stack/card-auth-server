package org.freedomfinancestack.razorpay.cas.contract;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Data;

@Data
public class CRES extends ThreeDSObject {

    // All Required Fields
    private String threeDSServerTransID;

    private String acsCounterAtoS;

    private String acsTransID;

    private String acsUiType;

    private String challengeCompletionInd;

    private String messageType = MessageType.CRes.toString();

    private String messageVersion;

    private String sdkTransID;

    // All Optional Fields
    private String acsHTMLRefresh;

    private String challengeAddInfo;

    private String challengeInfoHeader;

    private String challengeInfoLabel;

    private String challengeInfoText;

    private String challengeInfoTextIndicator;

    private ChallengeSelectInfo[] challengeSelectInfo;

    private String expandInfoLabel;

    private String expandInfoText;

    private String oobAppURL;

    // All Conditional Fields
    private String acsHTML;

    private Image issuerImage;

    private List<MessageExtension> messageExtension;

    private String oobAppLabel;

    private String oobContinue;

    private String oobContinueLabel;

    private Image psImage;

    private String resendInformationLabel;

    private String submitAuthenticationLabel;

    private String transStatus;

    private String whyInfoLabel;

    private String whyInfoText;

    private String whitelistingInfoText;

    @Override
    public MessageType getThreeDSMessageType() {
        return MessageType.CRes;
    }
}
