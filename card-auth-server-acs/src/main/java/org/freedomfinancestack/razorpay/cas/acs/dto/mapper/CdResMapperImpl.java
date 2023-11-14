package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionCardHolderDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code CdResMapperImpl} class provides method to populate CDRES object (challenge display
 * response) from the given transaction.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CdResMapperImpl {

    private final AppConfiguration appConfiguration;
    private final InstitutionRepository institutionRepository;

    /**
     * Updates the CDRES object (challenge display response) from the given transaction.
     *
     * @param cdRes the CDRES object which is passed as reference and will be update
     * @param transaction Transaction object to read data inorder to update CDRES
     * @throws DataNotFoundException
     */
    public void generateCDres(@NonNull final CdRes cdRes, final Transaction transaction)
            throws DataNotFoundException {
        cdRes.setTransactionId(transaction.getId());
        cdRes.setValidationUrl(
                Util.getAcsChallengeValidationUrl(
                        appConfiguration.getHostname(), transaction.getDeviceChannel()));
        Optional<Institution> institution =
                institutionRepository.findById(transaction.getInstitutionId());
        if (institution.isPresent()) {
            cdRes.setInstitutionName(institution.get().getName());
        } else {
            log.error("Institution not found for transaction: " + transaction.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.INSTITUTION_NOT_FOUND);
        }
        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
        cdRes.setSchemaName(network.getName());
        if (transaction.getTransactionBrowserDetail().getJavascriptEnabled() != null) {
            cdRes.setJsEnableIndicator(
                    transaction.getTransactionBrowserDetail().getJavascriptEnabled());
        }
        StringBuilder challengeText = new StringBuilder();
        TransactionCardHolderDetail transactionCardHolderDetail =
                transaction.getTransactionCardHolderDetail();
        boolean isContactInfoAvailable = false;
        if (!Util.isNullorBlank(transactionCardHolderDetail.getMobileNumber())) {
            isContactInfoAvailable = true;
            challengeText.append(
                    String.format(
                            InternalConstants.CHALLENGE_INFORMATION_MOBILE_TEXT,
                            transactionCardHolderDetail.getMobileNumber()));
        }
        if (!Util.isNullorBlank(transactionCardHolderDetail.getEmailId())) {
            if (isContactInfoAvailable) {
                challengeText.append(InternalConstants.AND);
            }
            isContactInfoAvailable = true;
            challengeText.append(
                    String.format(
                            InternalConstants.CHALLENGE_INFORMATION_EMAIL_TEXT,
                            transactionCardHolderDetail.getEmailId()));
        }
        if (!isContactInfoAvailable) {
            log.error(
                    "No contact information found for card user for transaction id "
                            + transaction.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.NO_CHANNEL_FOUND_FOR_OTP);
        }
        cdRes.setChallengeText(InternalConstants.CHALLENGE_INFORMATION_TEXT + challengeText);
    }
}
