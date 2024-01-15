package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {
            TransactionStatus.class,
            MessageCategory.class,
            Network.class,
            MessageType.class,
            DeviceChannel.class,
            Util.class,
            InternalConstants.class,
            ThreeDSConstant.class,
            RouteConstants.class
        })
public interface InstitutionUiParamsMapper {

    // TODO: inject configuration file automatically instead of passing to the function
    @Mapping(
            target = "isJSEnabled",
            expression =
                    "java(transaction.getDeviceChannel().equals(DeviceChannel.BRW.getChannel()) &&"
                        + " transaction.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_2_0)"
                        + " ? true : false)")
    @Mapping(
            target = "cardNumber",
            expression =
                    "java(Util.maskedCardNumber(transaction.getTransactionCardDetail().getCardNumber()))")
    @Mapping(target = "amount", expression = "java(getAmount(transaction))")
    @Mapping(target = "currency", expression = "java(getCurrency(transaction))")
    @Mapping(
            target = "merchantName",
            expression = "java(transaction.getTransactionMerchant().getMerchantName())")
    @Mapping(target = "deviceChannel", expression = "java(transaction.getDeviceChannel())")
    @Mapping(target = "timeout", expression = "java(180)")
    @Mapping(
            target = "validationUrl",
            expression =
                    "java(getValidationUrl(transaction,"
                            + " institutionUiConfiguration,"
                            + " appConfiguration))")
    @Mapping(
            target = "issuerImage",
            expression = "java(getIssuerImage(transaction," + " institutionUiConfiguration))")
    @Mapping(
            target = "psImage",
            expression = "java(getPsImage(transaction, institutionUiConfiguration))")
    @Mapping(target = "expandInfoLabel", source = "institutionUiConfig.expandInfoLabel")
    @Mapping(target = "expandInfoText", source = "institutionUiConfig.expandInfoText")
    @Mapping(target = "whyInfoLabel", source = "institutionUiConfig.whyInfoLabel")
    @Mapping(target = "whyInfoText", source = "institutionUiConfig.whyInfoText")
    @Mapping(target = "challengeInfoHeader", source = "institutionUiConfig.challengeInfoHeader")
    @Mapping(target = "challengeInfoLabel", source = "institutionUiConfig.challengeInfoLabel")
    @Mapping(
            target = "whitelistingInfoText",
            expression =
                    "java(getWhitelistingInfoText(transaction, authConfigDto,"
                            + " institutionUiConfig))")
    @Mapping(
            target = "otpLength",
            expression =
                    "java(authConfigDto.getOtpConfig() != null ?"
                            + " authConfigDto.getOtpConfig().getLength() : 0)")
    @Mapping(
            target = "challengeInfoText",
            expression =
                    "java(getChallengeInfoText(transaction, institutionUiConfig, authConfigDto,"
                            + " uiType, currentState))")
    @Mapping(
            target = "submitAuthenticationLabel",
            expression =
                    "java(!uiType.equals(UIType.OOB) ?"
                            + " institutionUiConfig.getSubmitAuthenticationLabel() : null)")
    @Mapping(
            target = "resendInformationLabel",
            expression =
                    "java(uiType.equals(UIType.TEXT) || uiType.equals(UIType.HTML_OTHER) ?"
                            + " institutionUiConfig.getResendInformationLabel() : null)")
    @Mapping(
            target = "challengeSelectInfo",
            expression = "java(getChallengeSelectInfo(testConfigProperties," + " uiType))")
    @Mapping(
            target = "oobContinueLabel",
            expression =
                    "java(uiType.equals(UIType.OOB) ? InternalConstants.OOB_CONTINUE_LABEL : null)")
    @Mapping(
            target = "resendAttemptLeft",
            expression = "java(getResendAttemptLeft(transaction, authConfigDto, currentState))")
    @Mapping(
            target = "otpAttemptLeft",
            expression = "java(getOtpAttemptLeft(transaction, authConfigDto, currentState))")
    @Mapping(target = "messageVersion", source = "transaction.messageVersion")
    @Mapping(target = "acsTransID", source = "transaction.id")
    @Mapping(
            target = "threeDSServerTransID",
            source = "transaction.transactionReferenceDetail.threedsServerTransactionId")
    @Mapping(target = "displayPage", expression = "java(null)")
    InstitutionUIParams toInstitutionUiParams(
            Transaction transaction,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto,
            UIType uiType,
            String currentState,
            AppConfiguration appConfiguration,
            InstitutionUiConfiguration institutionUiConfiguration,
            TestConfigProperties testConfigProperties);

    default String getAmount(Transaction transaction) {
        if (transaction.getMessageCategory().equals(MessageCategory.PA)) {
            String purchaseAmount = transaction.getTransactionPurchaseDetail().getPurchaseAmount();
            String exponent =
                    transaction.getTransactionPurchaseDetail().getPurchaseExponent().toString();

            return Util.formatAmount(purchaseAmount, exponent);
        }
        return null;
    }

    default String getCurrency(Transaction transaction) {
        if (transaction.getMessageCategory().equals(MessageCategory.PA)) {
            return Util.getCurrencyInstance(
                            transaction.getTransactionPurchaseDetail().getPurchaseCurrency())
                    .getCurrencyCode();
        }
        return null;
    }

    default String getValidationUrl(
            Transaction transaction,
            InstitutionUiConfiguration institutionUiConfiguration,
            AppConfiguration appConfiguration) {
        return transaction.getDeviceChannel().equals(DeviceChannel.BRW.getChannel())
                        || transaction
                                .getTransactionSdkDetail()
                                .getAcsInterface()
                                .equals(DeviceInterface.NATIVE.getValue())
                ? RouteConstants.getAcsChallengeValidationUrl(
                        appConfiguration.getHostname(), transaction.getDeviceChannel())
                : institutionUiConfiguration.getInstitutionCssUrl();
    }

    default Image getIssuerImage(
            Transaction transaction, InstitutionUiConfiguration institutionUiConfiguration) {
        Image issuerLogo = new Image();
        issuerLogo.setMedium(institutionUiConfiguration.getMediumLogo());
        issuerLogo.setHigh(institutionUiConfiguration.getHighLogo());
        issuerLogo.setExtraHigh(institutionUiConfiguration.getExtraHighLogo());

        return issuerLogo;
    }

    default Image getPsImage(
            Transaction transaction, InstitutionUiConfiguration institutionUiConfiguration) {
        Image psImage = new Image();
        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
        psImage.setMedium(
                institutionUiConfiguration.getNetworkUiConfig().get(network).getMediumPs());
        psImage.setHigh(institutionUiConfiguration.getNetworkUiConfig().get(network).getHighPs());
        psImage.setExtraHigh(
                institutionUiConfiguration.getNetworkUiConfig().get(network).getExtraHighPs());

        return psImage;
    }

    default String getWhitelistingInfoText(
            Transaction transaction,
            AuthConfigDto authConfigDto,
            InstitutionUiConfig institutionUiConfig) {
        return transaction
                                .getTransactionReferenceDetail()
                                .getThreeDSRequestorChallengeInd()
                                .equals(
                                        ThreeDSRequestorChallengeInd
                                                .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                                .getValue())
                        && authConfigDto.getChallengeAttemptConfig().isWhitelistingAllowed()
                ? institutionUiConfig.getWhitelistingInfoText()
                : null;
    }

    default String getChallengeInfoText(
            Transaction transaction,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto,
            UIType uiType,
            String currentState) {
        String challengeText = institutionUiConfig.getChallengeInfoText();

        if (uiType.equals(UIType.TEXT) || uiType.equals(UIType.HTML_OTHER)) {
            String mobileNumber =
                    Util.getLastFourDigit(
                            transaction.getTransactionCardHolderDetail().getMobileNumber());
            challengeText =
                    challengeText.replaceFirst(
                            InternalConstants.LAST_FOUR_DIGIT_MOBILE_NUMBER, mobileNumber);
        }

        if (currentState != null) {
            if (currentState.equals(InternalConstants.RESEND)) {
                challengeText =
                        challengeText.replaceFirst(
                                InternalConstants.SENT, InternalConstants.RESENT);
            } else if (currentState.equals(InternalConstants.VALIDATE_OTP)) {
                challengeText =
                        String.format(
                                InternalConstants.CHALLENGE_INCORRECT_OTP_TEXT,
                                authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                                        - transaction.getInteractionCount());
            }
        }
        return challengeText;
    }

    default ChallengeSelectInfo[] getChallengeSelectInfo(
            TestConfigProperties testConfigProperties, UIType uiType) {
        if (testConfigProperties.isEnable()
                && (uiType.equals(UIType.SINGLE_SELECT) || uiType.equals(UIType.MULTI_SELECT))) {
            return new ChallengeSelectInfo[] {
                ChallengeSelectInfo.builder().yes("yes").build(),
                ChallengeSelectInfo.builder().yes("no").build()
            };
        }
        return null;
    }

    default String getResendAttemptLeft(
            Transaction transaction, AuthConfigDto authConfigDto, String currentState) {
        if (currentState != null && currentState.equals(InternalConstants.RESEND)) {
            return String.valueOf(
                    authConfigDto.getChallengeAttemptConfig().getResendThreshold()
                            - transaction.getResendCount());
        }
        return null;
    }

    default String getOtpAttemptLeft(
            Transaction transaction, AuthConfigDto authConfigDto, String currentState) {
        if (currentState != null && currentState.equals(InternalConstants.VALIDATE_OTP)) {
            return String.valueOf(
                    authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                            - transaction.getInteractionCount());
        }
        return null;
    }
}
