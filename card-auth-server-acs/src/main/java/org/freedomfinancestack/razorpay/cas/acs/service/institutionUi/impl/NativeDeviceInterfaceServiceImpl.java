package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl;

import java.text.SimpleDateFormat;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.DeviceInterfaceService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
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

    @Override
    public void populateInstitutionUiConfig(
            Transaction transaction,
            AppChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig)
            throws ACSDataAccessException {

        // TODO Need to update it according to the Message Category

        InstitutionUiConfig validInstitutionUiConfig = new InstitutionUiConfig();

        String challengeText = null;

        UIType uiType = UIType.getUIType(transaction.getTransactionSdkDetail().getAcsUiType());

        String transactionDate =
                new SimpleDateFormat("dd/MM/yyyy").format(transaction.getModifiedAt());
        String cardNumber = transaction.getTransactionCardDetail().getCardNumber();

        MessageCategory messageCategory = transaction.getMessageCategory();
        String purchaseAmount = null;
        String exponent = null;
        String amount = null;
        String currency = null;
        if (messageCategory.equals(MessageCategory.PA)) {
            purchaseAmount = transaction.getTransactionPurchaseDetail().getPurchaseAmount();
            exponent = transaction.getTransactionPurchaseDetail().getPurchaseExponent().toString();
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

        challengeFlowDto.setIssuerImage(issuerLogo);
        challengeFlowDto.setPsImage(psImage);

        validInstitutionUiConfig.setChallengeInfoHeader(
                institutionUiConfig.getChallengeInfoHeader());
        validInstitutionUiConfig.setChallengeInfoLabel(institutionUiConfig.getChallengeInfoLabel());
        validInstitutionUiConfig.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
        validInstitutionUiConfig.setExpandInfoText(institutionUiConfig.getExpandInfoText());
        validInstitutionUiConfig.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
        validInstitutionUiConfig.setWhyInfoText(institutionUiConfig.getWhyInfoText());

        if (transaction
                .getTransactionReferenceDetail()
                .getThreeDSRequestorChallengeInd()
                .equals("09")) {
            validInstitutionUiConfig.setWhitelistingInfoText(
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

                validInstitutionUiConfig.setChallengeInfoText(challengeText);
                validInstitutionUiConfig.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());
                validInstitutionUiConfig.setResendInformationLabel(
                        institutionUiConfig.getResendInformationLabel());

                validInstitutionUiConfig.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());
                break;

            case SINGLE_SELECT:
                String username = transaction.getTransactionCardHolderDetail().getName();
                challengeText = institutionUiConfig.getChallengeInfoText();
                challengeText =
                        String.format(
                                challengeText,
                                username,
                                network.getName(),
                                institution.get().getName());
                validInstitutionUiConfig.setChallengeInfoText(challengeText);

                validInstitutionUiConfig.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());

                // TODO challengeSelectInfo
                break;

            case MULTI_SELECT:
                challengeText = institutionUiConfig.getChallengeInfoText();
                challengeText = String.format(challengeText, institution.get().getName());
                validInstitutionUiConfig.setChallengeInfoText(challengeText);

                validInstitutionUiConfig.setSubmitAuthenticationLabel(
                        institutionUiConfig.getSubmitAuthenticationLabel());

                // TODO challengeSelectInfo
                break;

            case OOB:
                challengeText = institutionUiConfig.getChallengeInfoText();

                validInstitutionUiConfig.setChallengeInfoText(challengeText);

                validInstitutionUiConfig.setResendInformationLabel(
                        institutionUiConfig.getResendInformationLabel());

                // TODO set OOB Constants
                break;

            case HTML_OTHER:
                throw new ACSDataAccessException(
                        InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        "UI Type Implementation not available with the given option " + uiType);

            default:
                throw new ACSDataAccessException(
                        InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        "UI Type Implementation not available with the given option " + uiType);
        }
        challengeFlowDto.setInstitutionUiConfig(validInstitutionUiConfig);
    }
}
