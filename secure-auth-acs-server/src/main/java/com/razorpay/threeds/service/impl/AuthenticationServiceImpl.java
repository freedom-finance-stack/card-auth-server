package com.razorpay.threeds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.razorpay.acs.contract.AREQ;
import com.razorpay.acs.contract.ARES;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.RiskFlag;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.acs.dao.model.InstitutionAcsUrl;
import com.razorpay.acs.dao.model.InstitutionAcsUrlPK;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.dto.AResMapperParams;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.dto.GenerateECIRequest;
import com.razorpay.threeds.dto.mapper.AResMapper;
import com.razorpay.threeds.exception.InternalErrorCode;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.exception.checked.ACSDataAccessException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.*;
import com.razorpay.threeds.service.authvalue.AuthValueGeneratorService;
import com.razorpay.threeds.service.cardDetail.CardDetailService;
import com.razorpay.threeds.utils.Util;
import com.razorpay.threeds.validator.ThreeDSValidator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("authenticationServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TransactionService transactionService;
    private final TransactionMessageTypeService transactionMessageTypeService;
    private final RangeService rangeService;
    private final CardDetailService cardDetailService;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final ECommIndicatorService eCommIndicatorService;
    private final AResMapper aResMapper;
    private final InstitutionAcsUrlService institutionAcsUrlService;

    @Qualifier(value = "authenticationRequestValidator") private final ThreeDSValidator<AREQ> areqValidator;

    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq)
            throws ThreeDSException, ACSDataAccessException {
        Transaction transaction = new Transaction();
        InstitutionAcsUrl acsUrl = null;
        ARES ares = null;
        CardRange cardRange = null;
        try {
            areq.setTransactionId(Util.generateUUID());
            transaction.setId(areq.getTransactionId());
            // log incoming request in DB
            transactionMessageTypeService.createAndSave(areq, areq.getTransactionId());
            // validate areq
            areqValidator.validateRequest(areq);

            // todo check duplicate transaction once threeDSmethod is implemented

            // create transaction entity and save
            transaction = transactionService.saveOrUpdate(transactionService.create(areq));

            // get range and institution entity and verify
            cardRange = rangeService.findByPan(areq.getAcctNumber());
            rangeService.validateRange(cardRange);
            transaction.getTransactionCardDetail().setNetworkCode(cardRange.getNetwork().getCode());

            // get acs url
            acsUrl =
                    institutionAcsUrlService.findById(
                            new InstitutionAcsUrlPK(
                                    cardRange.getCardRangeGroup().getInstitution().getId(),
                                    areq.getDeviceChannel(),
                                    cardRange.getNetwork().getCode()));

            CardDetailsRequest cardDetailsRequest =
                    new CardDetailsRequest(
                            cardRange.getCardRangeGroup().getInstitution().getId(),
                            areq.getAcctNumber());
            CardDetailResponse cardDetailResponse =
                    cardDetailService.getCardDetails(
                            cardDetailsRequest, cardRange.getCardDetailsStore());
            cardDetailService.validateCardDetails(
                    cardDetailResponse, cardRange.getCardDetailsStore());

            if (isChallengeRequired(cardRange.getRiskFlag(), transaction)) {
                transaction.setChallengeMandated(true);
                // todo add timer logic for challenge
                transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
            } else {
                transaction.setChallengeMandated(false);
                String eci =
                        eCommIndicatorService.generateECI(
                                new GenerateECIRequest(
                                                transaction.getTransactionStatus(),
                                                cardRange.getNetwork(),
                                                transaction.getMessageCategory())
                                        .setThreeRIInd(areq.getThreeRIInd()));
                transaction.setEci(eci);
                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                String authValue = authValueGeneratorService.getAuthValue(transaction);
                transaction.setAuthValue(authValue);
            }

        } catch (ThreeDSException ex) {
            transaction =
                    updateErrorAndSaveTransaction(
                            ex.getThreeDSecureErrorCode(),
                            ex.getInternalErrorCode(),
                            transaction,
                            ex);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
        } catch (ACSException ex) {
            transaction = updateErrorAndSaveTransaction(ex.getErrorCode(), transaction, ex);
        } catch (Exception ex) {
            transaction =
                    updateErrorAndSaveTransaction(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            transaction,
                            ex);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        }

        try {
            String eci =
                    eCommIndicatorService.generateECI(
                            new GenerateECIRequest(
                                            transaction.getTransactionStatus(),
                                            cardRange.getNetwork(),
                                            transaction.getMessageCategory())
                                    .setThreeRIInd(areq.getThreeRIInd()));
            transaction.setEci(eci);
            // Generate and Store Ares
            ares =
                    aResMapper.toAres(
                            areq,
                            transaction,
                            AResMapperParams.builder().acsUrl(acsUrl.getChallengeUrl()).build());
            transactionMessageTypeService.createAndSave(ares, areq.getTransactionId());
            transaction.setPhase(Phase.ARES);
        } catch (Exception ex) {
            transaction =
                    updateErrorAndSaveTransaction(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            transaction,
                            ex);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        } finally {
            transactionService.saveOrUpdate(transaction);
        }
        return ares;
    }

    private Transaction updateErrorAndSaveTransaction(
            ThreeDSecureErrorCode threeDSecureErrorCode,
            InternalErrorCode internalErrorCode,
            Transaction transaction,
            Exception ex)
            throws ACSDataAccessException {
        transaction.setErrorCode(threeDSecureErrorCode.getErrorCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setPhase(Phase.ERROR);
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        return transactionService.saveOrUpdate(transaction);
    }

    private Transaction updateErrorAndSaveTransaction(
            InternalErrorCode internalErrorCode, Transaction transaction, Exception ex)
            throws ACSDataAccessException {
        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        return transactionService.saveOrUpdate(transaction);
    }

    private boolean isChallengeRequired(RiskFlag riskFlag, Transaction transaction) {
        // todo honor ThreeDSRequestorChallsengeInd once RBA is implemented
        if (riskFlag.equals(RiskFlag.NO_CHALLENGE)) {
            return false;
        } else if (riskFlag.equals(RiskFlag.CHALLENGE)) {
            return true;
        } else { // RBA
            throw new UnsupportedOperationException("RBA is not supported yet");
        }
    }
}
