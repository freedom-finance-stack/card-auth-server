package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl;

import java.text.SimpleDateFormat;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.DeviceInterfaceService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.contract.enums.ThreeDSRequestorChallengeInd;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "nativeDeviceInterfaceServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NativeDeviceInterfaceServiceImpl implements DeviceInterfaceService {

    private final InstitutionRepository institutionRepository;

    private final InstitutionUiConfiguration institutionUiConfiguration;
    private final TestConfigProperties testConfigProperties;

    @Override
    public void generateAppUIParams(
            Transaction transaction,
            AppChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto)
            throws ACSDataAccessException {

        // TODO Need to update it according to the Message Category

        InstitutionUIParams validInstitutionUIParams = new InstitutionUIParams();

        String challengeText;

        UIType uiType = UIType.getUIType(transaction.getTransactionSdkDetail().getAcsUiTemplate());
        if (uiType == null) {
            throw new ACSDataAccessException(InternalErrorCode.UNSUPPORTED_UI_TYPE);
        }

        String transactionDate =
                new SimpleDateFormat("dd/MM/yyyy").format(transaction.getModifiedAt());
        String cardNumber = transaction.getTransactionCardDetail().getCardNumber();

        MessageCategory messageCategory = transaction.getMessageCategory();
        String amount = null;
        String currency = null;
        if (messageCategory.equals(MessageCategory.PA)) {
            String purchaseAmount = transaction.getTransactionPurchaseDetail().getPurchaseAmount();
            String exponent =
                    transaction.getTransactionPurchaseDetail().getPurchaseExponent().toString();
            amount = Util.formatAmount(purchaseAmount, exponent);
            currency = transaction.getTransactionPurchaseDetail().getPurchaseCurrency();
        }

        String mobileNumber =
                Util.getLastFourDigit(
                        transaction.getTransactionCardHolderDetail().getMobileNumber());
        String merchantName = transaction.getTransactionMerchant().getMerchantName();

        Optional<Institution> institution =
                institutionRepository.findById(transaction.getInstitutionId());
        if (institution.isEmpty()) {
            return;
        }

        String logoBaseUrl = institutionUiConfiguration.getInstitutionUrl();
        Image issuerLogo = new Image();
        issuerLogo.setMedium(logoBaseUrl + institutionUiConfiguration.getMediumLogo());
        issuerLogo.setHigh(logoBaseUrl + institutionUiConfiguration.getHighLogo());
        issuerLogo.setExtraHigh(logoBaseUrl + institutionUiConfiguration.getExtraHighLogo());

        Image psImage = new Image();
        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
        psImage.setMedium(
                logoBaseUrl
                        + institutionUiConfiguration
                                .getNetworkUiConfig()
                                .get(network)
                                .getMediumPs());
        psImage.setHigh(
                logoBaseUrl
                        + institutionUiConfiguration.getNetworkUiConfig().get(network).getHighPs());
        psImage.setExtraHigh(
                logoBaseUrl
                        + institutionUiConfiguration
                                .getNetworkUiConfig()
                                .get(network)
                                .getExtraHighPs());

        validInstitutionUIParams.setIssuerImage(issuerLogo);
        validInstitutionUIParams.setPsImage(psImage);

        validInstitutionUIParams.setChallengeInfoHeader(
                institutionUiConfig.getChallengeInfoHeader());
        validInstitutionUIParams.setChallengeInfoLabel(institutionUiConfig.getChallengeInfoLabel());
        validInstitutionUIParams.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
        validInstitutionUIParams.setExpandInfoText(institutionUiConfig.getExpandInfoText());
        validInstitutionUIParams.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
        validInstitutionUIParams.setWhyInfoText(institutionUiConfig.getWhyInfoText());

        if (transaction
                        .getTransactionReferenceDetail()
                        .getThreeDSRequestorChallengeInd()
                        .equals(
                                ThreeDSRequestorChallengeInd
                                        .WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED
                                        .getValue())
                && authConfigDto.getChallengeAttemptConfig().isWhitelistingAllowed()) {
            validInstitutionUIParams.setWhitelistingInfoText(
                    institutionUiConfig.getWhitelistingInfoText());
        }

        switch (uiType) {
            case TEXT:
                challengeText = institutionUiConfig.getChallengeInfoText();
                challengeText =
                        challengeText.replaceFirst(
                                InternalConstants.LAST_FOUR_DIGIT_MOBILE_NUMBER, mobileNumber);
                challengeText =
                        challengeText.replaceFirst(
                                InternalConstants.MASKED_CARD_NUMBER, cardNumber);
                challengeText =
                        challengeText.replaceFirst(InternalConstants.MERCHANT_NAME, merchantName);
                if (messageCategory.equals(MessageCategory.PA)) {
                    challengeText =
                            challengeText.replaceFirst(
                                    InternalConstants.AMOUNT_WITH_CURRENCY,
                                    amount + InternalConstants.SPACE + currency);
                }
                challengeText =
                        challengeText.replaceFirst(
                                InternalConstants.TRANSACTION_DATE, transactionDate);

                validInstitutionUIParams.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());
                validInstitutionUIParams.setResendInformationLabel(
                        institutionUiConfig.getResendInformationLabel());

                validInstitutionUIParams.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());
                break;
            case SINGLE_SELECT:
            case MULTI_SELECT:
                challengeText = institutionUiConfig.getChallengeInfoText();

                validInstitutionUIParams.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());
                if (testConfigProperties.isEnable()) {
                    validInstitutionUIParams.setChallengeSelectInfo(
                            new ChallengeSelectInfo[] {
                                ChallengeSelectInfo.builder().yes("yes").build(),
                                ChallengeSelectInfo.builder().yes("no").build()
                            });
                }
                break;

            case OOB:
                challengeText = institutionUiConfig.getChallengeInfoText();

                validInstitutionUIParams.setResendInformationLabel(
                        institutionUiConfig.getResendInformationLabel());
                validInstitutionUIParams.setOobContinueLabel(InternalConstants.OOB_CONTINUE_LABEL);

                break;

                // This handles HTML_OTHER cases too
            default:
                throw new ACSDataAccessException(
                        InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        "UI Type Implementation not available with the given option " + uiType);
        }
        validInstitutionUIParams.setChallengeInfoText(challengeText);
        challengeFlowDto.setInstitutionUIParams(validInstitutionUIParams);
    }
}
