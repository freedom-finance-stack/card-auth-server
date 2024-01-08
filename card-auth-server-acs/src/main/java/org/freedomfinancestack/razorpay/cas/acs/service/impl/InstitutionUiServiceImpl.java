package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.UiConfigException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.InstitutionUiService;
import org.freedomfinancestack.razorpay.cas.acs.service.ThymeleafService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ChallengeSelectInfo;
import org.freedomfinancestack.razorpay.cas.contract.Image;
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfigPK;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionUiConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "InstitutionUiServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionUiServiceImpl implements InstitutionUiService {

    private final InstitutionUiConfigRepository institutionUiConfigRepository;
    private final AppConfiguration appConfiguration;
    private final InstitutionUiConfiguration institutionUiConfiguration;
    private final TestConfigProperties testConfigProperties;
    private final ThymeleafService thymeleafService;

    @Override
    public void populateUiParams(
            ChallengeFlowDto challengeFlowDto, Transaction transaction, AuthConfigDto authConfigDto)
            throws UiConfigException {

        DeviceInterface deviceInterface =
                DeviceInterface.getDeviceInterface(
                        transaction.getTransactionSdkDetail().getAcsInterface());
        AuthType authType = AuthType.getAuthType(transaction.getAuthenticationType());

        UIType uiType =
                transaction.getDeviceChannel().equals(DeviceChannel.BRW.getChannel())
                        ? UIType.TEXT
                        : UIType.getUIType(
                                transaction.getTransactionSdkDetail().getAcsUiTemplate());
        Optional<InstitutionUiConfig> institutionUiConfig =
                institutionUiConfigRepository.findById(
                        new InstitutionUiConfigPK(
                                transaction.getInstitutionId(), authType, uiType));

        if (institutionUiConfig.isPresent()) {
            populateUiParamsHandler(
                    transaction, challengeFlowDto, institutionUiConfig.get(), authConfigDto);
            return;
        }

        log.error(
                "Institution Ui Config not found for Institution ID : "
                        + transaction.getInstitutionId());
        throw new UiConfigException(
                InternalErrorCode.INSTITUTION_UI_CONFIG_NOT_FOUND,
                "Institution Ui Config not found");
    }

    @Override
    public String getEncodedHtml(InstitutionUIParams institutionUIParams) throws UiConfigException {
        String html;
        html =
                thymeleafService.getOtpHTMLPage(
                        institutionUIParams, institutionUiConfiguration.getHtmlOtpTemplate());

        if (html == null) {
            throw new UiConfigException(
                    InternalErrorCode.DISPLAY_PAGE_NOT_FOUND, "can not create html display page");
        }
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(html.getBytes(StandardCharsets.UTF_8));
    }

    private void populateUiParamsHandler(
            Transaction transaction,
            ChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto)
            throws UiConfigException {

        InstitutionUIParams validInstitutionUIParams = new InstitutionUIParams();

        String challengeText;
        UIType uiType;
        if (transaction.getDeviceChannel().equals(DeviceChannel.BRW.getChannel())) {
            uiType = UIType.TEXT;
        } else {
            uiType = UIType.getUIType(transaction.getTransactionSdkDetail().getAcsUiType());
            if (uiType == null) {
                throw new UiConfigException(
                        InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        InternalErrorCode.UNSUPPORTED_UI_TYPE.getDefaultErrorMessage());
            }
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
            validInstitutionUIParams.setAmount(amount);
            validInstitutionUIParams.setCurrency(currency);
        }

        String mobileNumber =
                Util.getLastFourDigit(
                        transaction.getTransactionCardHolderDetail().getMobileNumber());
        String merchantName = transaction.getTransactionMerchant().getMerchantName();

        validInstitutionUIParams.setMerchantName(merchantName);
        validInstitutionUIParams.setCardNumber(Util.maskedCardNumber(cardNumber));
        validInstitutionUIParams.setValidationUrl(
                RouteConstants.getAcsChallengeValidationUrl(
                        appConfiguration.getHostname(), transaction.getDeviceChannel()));

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

                validInstitutionUIParams.setOobContinueLabel(InternalConstants.OOB_CONTINUE_LABEL);

                break;

            case HTML_OTHER:
                challengeText = institutionUiConfig.getChallengeInfoText();

                validInstitutionUIParams.setValidationUrl(
                        institutionUiConfiguration.getInstitutionCssUrl());

                break;

            default:
                throw new UiConfigException(
                        InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        "UI Type Implementation not available with the given option " + uiType);
        }

        if (!uiType.equals(UIType.HTML_OTHER)) {
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
                            + institutionUiConfiguration
                                    .getNetworkUiConfig()
                                    .get(network)
                                    .getHighPs());
            psImage.setExtraHigh(
                    logoBaseUrl
                            + institutionUiConfiguration
                                    .getNetworkUiConfig()
                                    .get(network)
                                    .getExtraHighPs());
            validInstitutionUIParams.setIssuerImage(issuerLogo);
            validInstitutionUIParams.setPsImage(psImage);
            validInstitutionUIParams.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
            validInstitutionUIParams.setExpandInfoText(institutionUiConfig.getExpandInfoText());
            validInstitutionUIParams.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
            validInstitutionUIParams.setWhyInfoText(institutionUiConfig.getWhyInfoText());
            validInstitutionUIParams.setChallengeInfoHeader(
                    institutionUiConfig.getChallengeInfoHeader());
            validInstitutionUIParams.setChallengeInfoLabel(
                    institutionUiConfig.getChallengeInfoLabel());
            validInstitutionUIParams.setChallengeInfoText(challengeText);
        }

        validInstitutionUIParams.setMessageVersion(transaction.getMessageVersion());
        validInstitutionUIParams.setAcsTransID(transaction.getId());
        validInstitutionUIParams.setThreeDSServerTransID(
                transaction.getTransactionReferenceDetail().getThreedsServerTransactionId());
        challengeFlowDto.setInstitutionUIParams(validInstitutionUIParams);
    }
}
