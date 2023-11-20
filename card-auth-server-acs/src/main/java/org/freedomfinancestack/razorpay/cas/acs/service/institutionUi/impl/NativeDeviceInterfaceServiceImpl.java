package org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AppChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.institutionUi.DeviceInterfaceService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionUiConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Slf4j
@Service(value = "nativeDeviceInterfaceServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NativeDeviceInterfaceServiceImpl implements DeviceInterfaceService {

    private final InstitutionRepository institutionRepository;

    @Override
    public void populateInstitutionUiConfig(Transaction transaction, AppChallengeFlowDto challengeFlowDto, InstitutionUiConfig institutionUiConfig) throws ACSDataAccessException {

        InstitutionUiConfig validInstitutionUiConfig = new InstitutionUiConfig();

        String challengeText = null;

        UIType uiType = UIType.getUIType(transaction.getTransactionSdkDetail().getAcsUiType());

        String transactionDate = new SimpleDateFormat("dd/MM/yyy").format(transaction.getModifiedAt());
        String cardNumber = transaction.getTransactionCardDetail().getCardNumber();

        String purchaseAmount = transaction.getTransactionPurchaseDetail().getPurchaseAmount();
        String exponent = transaction.getTransactionPurchaseDetail().getPurchaseExponent().toString();
        String amount = Util.formatAmount(purchaseAmount, exponent);
        String currency = transaction.getTransactionPurchaseDetail().getPurchaseCurrency();

        String mobileNumber = Util.getLastFourDigit(transaction.getTransactionCardHolderDetail().getMobileNumber());
        String merchantName = transaction.getTransactionMerchant().getMerchantName();

        Optional<Institution> institution = institutionRepository.findById(transaction.getInstitutionId());
        if (institution.isEmpty()) {
            return;
        }

        switch (uiType) {
            case TEXT:
                // issuer logo image
                validInstitutionUiConfig.setChallengeInfoHeader(institutionUiConfig.getChallengeInfoHeader());
                validInstitutionUiConfig.setChallengeInfoLabel(institutionUiConfig.getChallengeInfoLabel());

                challengeText = institutionUiConfig.getChallengeInfoText();

                challengeText = challengeText.replaceFirst(InternalConstants.LAST_FOUR_DIGIT_MOBILE_NUMBER, mobileNumber);
                challengeText = challengeText.replaceFirst(InternalConstants.MASKED_CARD_NUMBER, cardNumber);
                challengeText = challengeText.replaceFirst(InternalConstants.MERCHANT_NAME, merchantName);
                challengeText = challengeText.replaceFirst(InternalConstants.AMOUNT_WITH_CURRENCY, amount + InternalConstants.SPACE + currency);
                challengeText = challengeText.replaceFirst(InternalConstants.TRANSACTION_DATE, transactionDate);

                validInstitutionUiConfig.setChallengeInfoText(challengeText);
                validInstitutionUiConfig.setSubmitAuthenticationLabel(institutionUiConfig.getSubmitAuthenticationLabel());
                validInstitutionUiConfig.setResendInformationLabel(institutionUiConfig.getResendInformationLabel());
                validInstitutionUiConfig.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
                validInstitutionUiConfig.setWhyInfoText(institutionUiConfig.getWhyInfoText());
                validInstitutionUiConfig.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
                validInstitutionUiConfig.setExpandInfoText(institutionUiConfig.getExpandInfoText());
                break;

            case SINGLE_SELECT:
                validInstitutionUiConfig.setChallengeInfoHeader(institutionUiConfig.getChallengeInfoHeader());

                String username = transaction.getTransactionCardHolderDetail().getName();
                Network network = Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
                challengeText = institutionUiConfig.getChallengeInfoText();
                challengeText = String.format(challengeText, username, network.getName(), institution.get().getName());
                validInstitutionUiConfig.setChallengeInfoText(challengeText);

//                ChallengeSelectInfo challengeSelectInfo = new ChallengeSelectInfo();
                validInstitutionUiConfig.setSubmitAuthenticationLabel(institutionUiConfig.getSubmitAuthenticationLabel());

                validInstitutionUiConfig.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
                validInstitutionUiConfig.setWhyInfoText(institutionUiConfig.getWhyInfoText());
                validInstitutionUiConfig.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
                validInstitutionUiConfig.setExpandInfoText(institutionUiConfig.getExpandInfoText());

//                if(AuthType.ExternalOTP.getCode().equals(institutionUiConfig.getInstitutionUiConfigPK().getAuthType())) {
//                    challengeSelectInfo.setSms("SMS");
//                    challengeSelectInfo.setEmail("Email");
//                    transactionVO.setChallengeSelectInfo(new ChallengeSelectInfo[] {challengeSelectInfo});
//                }
                break;

            case MULTI_SELECT:
                validInstitutionUiConfig.setChallengeInfoHeader(institutionUiConfig.getChallengeInfoHeader());
                validInstitutionUiConfig.setChallengeInfoLabel(institutionUiConfig.getChallengeInfoLabel());

                challengeText = institutionUiConfig.getChallengeInfoText();
                challengeText = String.format(challengeText, institution.get().getName());
                validInstitutionUiConfig.setChallengeInfoText(challengeText);

                validInstitutionUiConfig.setSubmitAuthenticationLabel(institutionUiConfig.getSubmitAuthenticationLabel());
                validInstitutionUiConfig.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
                validInstitutionUiConfig.setWhyInfoText(institutionUiConfig.getWhyInfoText());
                validInstitutionUiConfig.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
                validInstitutionUiConfig.setExpandInfoText(institutionUiConfig.getExpandInfoText());
                break;

            case OOB:
                // issuer image
                validInstitutionUiConfig.setChallengeInfoHeader(institutionUiConfig.getChallengeInfoHeader());
                validInstitutionUiConfig.setChallengeInfoLabel(institutionUiConfig.getChallengeInfoLabel());
                challengeText = institutionUiConfig.getChallengeInfoText();

                validInstitutionUiConfig.setChallengeInfoText(challengeText);
                validInstitutionUiConfig.setSubmitAuthenticationLabel(institutionUiConfig.getSubmitAuthenticationLabel());
                validInstitutionUiConfig.setResendInformationLabel(institutionUiConfig.getResendInformationLabel());
                validInstitutionUiConfig.setWhyInfoLabel(institutionUiConfig.getWhyInfoLabel());
                validInstitutionUiConfig.setWhyInfoText(institutionUiConfig.getWhyInfoText());
                validInstitutionUiConfig.setExpandInfoLabel(institutionUiConfig.getExpandInfoLabel());
                validInstitutionUiConfig.setExpandInfoText(institutionUiConfig.getExpandInfoText());
                // TODO setOobContinueLabel("Continue");
                break;

            case HTML_OTHER:
                throw new ACSDataAccessException(InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        "UI Type Implementation not available with the given option " + uiType);

            default:
                throw new ACSDataAccessException(InternalErrorCode.UNSUPPORTED_UI_TYPE,
                        "UI Type Implementation not available with the given option " + uiType);

        }
        challengeFlowDto.setInstitutionUiConfig(validInstitutionUiConfig);
    }
}
