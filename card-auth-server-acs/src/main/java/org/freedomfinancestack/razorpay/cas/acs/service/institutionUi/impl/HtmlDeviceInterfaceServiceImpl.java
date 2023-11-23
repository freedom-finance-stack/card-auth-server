package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.DeviceInterfaceService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
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
@Service(value = "htmlDeviceInterfaceServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HtmlDeviceInterfaceServiceImpl implements DeviceInterfaceService {

    private final InstitutionRepository institutionRepository;

    private final InstitutionUiConfiguration institutionUiConfiguration;

    private final AppConfiguration appConfiguration;

    @Override
    public void populateInstitutionUiConfig(
            Transaction transaction,
            AppChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig)
            throws ACSDataAccessException {
        InstitutionUiConfig validInstitutionUiConfig = new InstitutionUiConfig();

        Optional<Institution> institution =
                institutionRepository.findById(transaction.getInstitutionId());
        if (institution.isEmpty()) {
            return;
        }
        String institutionName = institution.get().getName();
        String timeoutInMinutes =
                String.valueOf(
                        appConfiguration.getAcs().getTimeout().getChallengeValidation() * 60);
        String timeoutInSeconds =
                String.valueOf(appConfiguration.getAcs().getTimeout().getChallengeValidation());

        String logoBaseUrl = institutionUiConfiguration.getInstitutionUrl();
        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());

        String issuerImage = logoBaseUrl + institutionUiConfiguration.getMediumLogo();
        String psImage =
                logoBaseUrl
                        + institutionUiConfiguration
                                .getNetworkUiConfig()
                                .get(network)
                                .getMediumPs();

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

        String cssUrl = institutionUiConfiguration.getInstitutionCssUrl();

        String html =
                institutionUiConfiguration.getNetworkUiConfig().get(network).getHtmlTemplateOtp();

        html = html.replaceAll(InternalConstants.INSTITUTION_NAME, institutionName);
        html = html.replaceAll(InternalConstants.INSTITUTION_TIMEOUT_IN_MINUTES, timeoutInMinutes);
        html = html.replaceAll(InternalConstants.INSTITUTION_TIMEOUT_IN_SECONDS, timeoutInSeconds);

        html = html.replaceFirst(InternalConstants.INSTITUTION_CSS_URL, cssUrl);
        html = html.replaceFirst(InternalConstants.PS_LOGO, psImage);
        html = html.replaceFirst(InternalConstants.INSTITUTION_LOGO, issuerImage);

        html = html.replaceFirst(InternalConstants.LAST_FOUR_DIGIT_MOBILE_NUMBER, mobileNumber);
        html = html.replaceFirst(InternalConstants.MASKED_CARD_NUMBER, cardNumber);
        html = html.replaceFirst(InternalConstants.MERCHANT_NAME, merchantName);
        if (messageCategory.equals(MessageCategory.PA)) {
            html =
                    html.replaceFirst(
                            InternalConstants.AMOUNT_WITH_CURRENCY,
                            amount + InternalConstants.SPACE + currency);
        }
        html = html.replaceFirst(InternalConstants.TRANSACTION_DATE, transactionDate);

        String encodedHtml =
                Base64.getUrlEncoder().encodeToString(html.getBytes()).replaceAll("=+$", "");
        validInstitutionUiConfig.setDisplayPage(encodedHtml);
        challengeFlowDto.setInstitutionUiConfig(validInstitutionUiConfig);
    }
}
