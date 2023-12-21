package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.InstitutionUiConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.service.ThymeleafService;
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

    private final ThymeleafService thymeleafService;

    @Override
    public void generateUIParams(
            Transaction transaction,
            ChallengeFlowDto challengeFlowDto,
            InstitutionUiConfig institutionUiConfig,
            AuthConfigDto authConfigDto)
            throws ACSDataAccessException {
        InstitutionUIParams validInstitutionUiParams = new InstitutionUIParams();

        AppOtpHtmlParams appOtpHtmlParams = new AppOtpHtmlParams();

        Optional<Institution> institution =
                institutionRepository.findById(transaction.getInstitutionId());
        if (institution.isEmpty()) {
            throw new ACSDataAccessException(InternalErrorCode.INSTITUTION_NOT_FOUND);
        }
        appOtpHtmlParams.setInstitutionName(institution.get().getName());
        appOtpHtmlParams.setTimeoutInSeconds(
                String.valueOf(institutionUiConfiguration.getHtmlPageTimer()));
        appOtpHtmlParams.setTimeoutInMinutes(
                String.valueOf(institutionUiConfiguration.getHtmlPageTimer() / 60));

        String logoBaseUrl = institutionUiConfiguration.getInstitutionUrl();
        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
        appOtpHtmlParams.setSchemaName(network.getName());
        appOtpHtmlParams.setIssuerImage(logoBaseUrl + institutionUiConfiguration.getMediumLogo());
        appOtpHtmlParams.setPsImage(
                logoBaseUrl
                        + institutionUiConfiguration
                                .getNetworkUiConfig()
                                .get(network)
                                .getMediumPs());

        appOtpHtmlParams.setTransactionDate(
                new SimpleDateFormat("dd/MM/yyyy").format(transaction.getModifiedAt()));
        appOtpHtmlParams.setCardNumber(transaction.getTransactionCardDetail().getCardNumber());

        MessageCategory messageCategory = transaction.getMessageCategory();
        if (messageCategory.equals(MessageCategory.PA)) {
            String purchaseAmount = transaction.getTransactionPurchaseDetail().getPurchaseAmount();
            String exponent =
                    transaction.getTransactionPurchaseDetail().getPurchaseExponent().toString();
            String amount = Util.formatAmount(purchaseAmount, exponent);
            String currency = transaction.getTransactionPurchaseDetail().getPurchaseCurrency();
            appOtpHtmlParams.setAmountWithCurrency(amount + InternalConstants.SPACE + currency);
        }

        appOtpHtmlParams.setMobileNumber(
                Util.getLastFourDigit(
                        transaction.getTransactionCardHolderDetail().getMobileNumber()));
        appOtpHtmlParams.setMerchantName(transaction.getTransactionMerchant().getMerchantName());

        appOtpHtmlParams.setValidationUrl(institutionUiConfiguration.getInstitutionCssUrl());

        String html;
        html =
                thymeleafService.getAppOtpHTML(
                        appOtpHtmlParams, institutionUiConfiguration.getHtmlOtpTemplate());

        if (html == null) {
            throw new ACSDataAccessException(InternalErrorCode.DISPLAY_PAGE_NOT_FOUND);
        }

        String encodedHtml =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(html.getBytes(StandardCharsets.UTF_8));
        validInstitutionUiParams.setDisplayPage(encodedHtml);
        challengeFlowDto.setInstitutionUIParams(validInstitutionUiParams);
    }
}
