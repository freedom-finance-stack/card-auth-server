package com.razorpay.threeds.service.impl;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
import com.razorpay.acs.dao.enums.AuthType;
import com.razorpay.acs.dao.enums.TransactionStatus;
import com.razorpay.acs.dao.model.CardRange;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.dto.CardDetailResponse;
import com.razorpay.threeds.dto.CardDetailsRequest;
import com.razorpay.threeds.service.AuthValueGenerator.AuthValueGeneratorService;
import com.razorpay.threeds.service.AuthenticationService;
import com.razorpay.threeds.service.RangeService;
import com.razorpay.threeds.service.TransactionMessageTypeService;
import com.razorpay.threeds.service.TransactionService;
import com.razorpay.threeds.service.cardDetail.CardDetailService;
import com.razorpay.threeds.utils.Util;
import com.razorpay.threeds.validator.ThreeDSValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("authenticationServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TransactionService transactionService;
    private final TransactionMessageTypeService transactionMessageTypeService;
    private final RangeService rangeService;
    private final CardDetailService cardDetailService;
    private final AuthValueGeneratorService authValueGeneratorService;

    @Qualifier(value = "authenticationRequestValidator")
    private final ThreeDSValidator<AREQ> areqValidator;

    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq) {
        Transaction transaction = null;
        try {
            areq.setTransactionId(Util.generateUUID());
            // log incoming request in DB
            transactionMessageTypeService.createAndSave(areq, areq.getTransactionId());
            // validate areq
            areqValidator.validateRequest(areq);

            // todo check duplicate transaction once threeDSmethod is implemented


            // create transaction entity and save
            transaction = transactionService.create(areq);
            transactionService.save(transaction);

            // get range and institution entity and verify
            CardRange cardRange = rangeService.findByPan(areq.getAcctNumber());
            rangeService.validateRange(cardRange);
            transaction.getTransactionCardDetail().setNetworkCode(cardRange.getNetworkCode());

            CardDetailsRequest cardDetailsRequest = new CardDetailsRequest(cardRange.getRangeGroup().getInstitution().getId(), areq.getAcctNumber()) ;
            CardDetailResponse cardDetailResponse = cardDetailService.getCardDetails(cardDetailsRequest, cardRange.getCardStoreType());
            cardDetailService.validateCardDetails(cardDetailResponse, cardRange.getCardStoreType());

            //todo handle INFORMATIONAL and ATTEMPT status
            if ( isChallengeRequired(cardRange.getAuthType(), transaction) ) {
                // todo add timer logic for challenge
            } else {
                String authValue = authValueGeneratorService.generateCAVV(transaction);
                transaction.setAuthValue(authValue);
            }


        } catch (Exception e) {
            // todo handle exception properly
            e.printStackTrace();
        }



        // auth validation service
        // riskBasedEngineService.determineChallenge(reqAReq, transaction);

//eci = eCommIndicatorService.generateECI(transaction, reqAReq);
//
//			//String eci = eCommIndicatorService.generateECI(transaction.getTransactionStatus(), transaction.getBrand().getLable());
//			LOGGER.trace(Utility.prefixTxnId(transaction.getTransactionId(),"Generated ECI : "+eci));
//			transaction.setEci(eci);
//
//
//			if (TransactionStatus.SUCCESS.equals(transaction.getTransactionStatus())
//					|| TransactionStatus.ATTEMPT.equals(transaction.getTransactionStatus())
//					|| TransactionStatus.INFORMATIONAL.equals(transaction.getTransactionStatus())) {
//				try {
//					LOGGER.trace(Utility.prefixTxnId(transaction.getTransactionId(),"Generating CAVV "));
//					HSMConfigPK configPK = new HSMConfigPK();
//					configPK.setInstitutionId(institution.getInstitutionId());
//					configPK.setNetworkId(instrumentDetail.getNetwork().getNetworkId());
//					LOGGER.trace(Utility.prefixTxnId(transaction.getTransactionId(),"HSM Config : "+configPK));
//					HSMConfig hsmConfig = hsmConfigService.findById(configPK);
//
//					String authValue = cavvGeneratorLocator.generateCAVV(reqAReq, hsmConfig, transaction, "nextval");
//
//					LOGGER.info(Utility.prefixTxnId(transaction.getTransactionId(), "Generated Auth Value ", authValue));
//					transaction.setAuthenticationValue(authValue);
//				} catch (ACSException e) {
//					e.printStackTrace();
//					LOGGER.error("Unable to generate Auth Value", e);
//				}
//			}

        // Store transaction details in db and get correct exception with details
        //generateARES
        // check every error and state being stored in db check for checked and unchecked exception... checked should return 200 with Ares
        // check transaction status handle

        return null;
    }

    private boolean isChallengeRequired(AuthType authType, Transaction transaction) {
        // todo honor ThreeDSRequestorChallengeInd once RBA is implemented
        if(authType.equals(AuthType.NO_CHALLENGE)) {
                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                transaction.setChallengeMandated(false);
                return true;
        } else if (authType.equals(AuthType.CHALLENGE)) {
                transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
                transaction.setChallengeMandated(true);
                return false;
        }else { // RBA
                throw new UnsupportedOperationException("RBA is not supported yet");
        }
    }


}
