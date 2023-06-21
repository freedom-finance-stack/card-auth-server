package com.razorpay.threeds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
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
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.*;
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
  public ARES processAuthenticationRequest(@NonNull AREQ areq) {
    Transaction transaction = null;
    InstitutionAcsUrl acsUrl = null;
    ARES ares = null;
    CardRange cardRange = null;
    try {
      areq.setTransactionId(Util.generateUUID());
      // log incoming request in DB
      transactionMessageTypeService.createAndSave(areq, areq.getTransactionId());
      // validate areq
      areqValidator.validateRequest(areq);

      // todo check duplicate transaction once threeDSmethod is implemented

      // create transaction entity and save
      transaction = transactionService.create(areq);
      transaction = transactionService.save(transaction);

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
              cardRange.getCardRangeGroup().getInstitution().getId(), areq.getAcctNumber());
      CardDetailResponse cardDetailResponse =
          cardDetailService.getCardDetails(cardDetailsRequest, cardRange.getCardDetailsStore());
      cardDetailService.validateCardDetails(cardDetailResponse, cardRange.getCardDetailsStore());

      // todo handle INFORMATIONAL and ATTEMPT status
      if (isChallengeRequired(cardRange.getRiskFlag(), transaction)) {
        // todo add timer logic for challenge
      } else {
        String authValue = authValueGeneratorService.generateCAVV(transaction);
        transaction.setAuthValue(authValue);
      }

    } catch (ACSException e) {
      // 3
      // Update transaction entity
    } catch (ThreeDSException e) {
      // 3
      // Update transaction entity

      // adding transaction data in exception
      transactionService.save(transaction);
      throw new ThreeDSException(e.getErrorCode(), e.getMessage(), transaction, e);
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
    } finally {
      transactionService.save(transaction);
    }

    // check transaction shouldn't be in created state
    // Store transaction details in db and get correct exception with details
    // check every error and state being stored in db check for checked and unchecked exception...
    // checked should return 200 with Ares
    // check transaction status handle
    // fix save transaction in finally, check save and flush
    return ares;
  }

  private boolean isChallengeRequired(RiskFlag riskFlag, Transaction transaction) {
    // todo honor ThreeDSRequestorChallengeInd once RBA is implemented
    if (riskFlag.equals(RiskFlag.NO_CHALLENGE)) {
      transaction.setTransactionStatus(TransactionStatus.SUCCESS);
      transaction.setChallengeMandated(false);
      return false;
    } else if (riskFlag.equals(RiskFlag.CHALLENGE)) {
      transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
      transaction.setChallengeMandated(true);
      return true;
    } else { // RBA
      throw new UnsupportedOperationException("RBA is not supported yet");
    }
  }
}
