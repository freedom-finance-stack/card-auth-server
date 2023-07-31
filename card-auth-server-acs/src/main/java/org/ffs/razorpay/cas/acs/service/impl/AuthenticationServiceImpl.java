package org.ffs.razorpay.cas.acs.service.impl;

import org.ffs.razorpay.cas.acs.dto.AResMapperParams;
import org.ffs.razorpay.cas.acs.dto.CardDetailResponse;
import org.ffs.razorpay.cas.acs.dto.CardDetailsRequest;
import org.ffs.razorpay.cas.acs.dto.GenerateECIRequest;
import org.ffs.razorpay.cas.acs.dto.mapper.AResMapper;
import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.acs.exception.ThreeDSException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSException;
import org.ffs.razorpay.cas.acs.service.AuthenticationService;
import org.ffs.razorpay.cas.acs.service.ECommIndicatorService;
import org.ffs.razorpay.cas.acs.service.InstitutionAcsUrlService;
import org.ffs.razorpay.cas.acs.service.RangeService;
import org.ffs.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.ffs.razorpay.cas.acs.service.TransactionService;
import org.ffs.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.ffs.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.ffs.razorpay.cas.acs.utils.Util;
import org.ffs.razorpay.cas.acs.validator.ThreeDSValidator;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.contract.ARES;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.ffs.razorpay.cas.dao.enums.Phase;
import org.ffs.razorpay.cas.dao.enums.RiskFlag;
import org.ffs.razorpay.cas.dao.enums.TransactionStatus;
import org.ffs.razorpay.cas.dao.model.CardRange;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.ffs.razorpay.cas.dao.model.InstitutionAcsUrlPK;
import org.ffs.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        ARES ares;
        CardRange cardRange = null;
        try {
            areq.setTransactionId(Util.generateUUID());
            transaction.setId(areq.getTransactionId());
            // log incoming request in DB
            transactionMessageTypeService.createAndSave(areq, areq.getTransactionId());
            // validate areq
            areqValidator.validateRequest(areq);

            // todo check duplicate transaction once threeDSmethod is implemented
            transaction = transactionService.create(areq);
            // create transaction entity and save
            transaction = transactionService.saveOrUpdate(transaction);

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
            log.error(" Message {}, Internal Error code {}", ex.getMessage(), ex.getInternalErrorCode());
            transaction =
                    updateTransactionPhaseWithError(
                            ex.getThreeDSecureErrorCode(), ex.getInternalErrorCode(), transaction);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
        } catch (ACSException ex) {
            log.error(" Message {}, Internal Error code {}", ex.getMessage(), ex.getErrorCode());
            transaction = updateTransactionForACSException(ex.getErrorCode(), transaction);
        } catch (Exception ex) {
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            transaction =
                    updateTransactionPhaseWithError(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            transaction);
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
                    updateTransactionPhaseWithError(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        } finally {
            transactionService.saveOrUpdate(transaction);
        }
        return ares;
    }

    private Transaction updateTransactionPhaseWithError(
            ThreeDSecureErrorCode threeDSecureErrorCode,
            InternalErrorCode internalErrorCode,
            Transaction transaction)
            throws ACSDataAccessException {
        transaction.setErrorCode(threeDSecureErrorCode.getErrorCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setPhase(Phase.ERROR);
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        return transactionService.saveOrUpdate(transaction);
    }

    private Transaction updateTransactionForACSException(
            InternalErrorCode internalErrorCode, Transaction transaction)
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
