package com.razorpay.threeds.validation.impl;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.threeds.validation.ValidationService;

import org.springframework.stereotype.Service;

@Service("aReqValidationServiceImpl")
public class AReqValidationServiceImpl implements ValidationService<AREQ> {

    @Override
    public void validate(AREQ areq) {
        // chain design pattern...  
        // if (reqAReq.getMessageType().equals(ThreeDSConstant.MESSAGE_TYPE_AREQ)
        // check for duplicate request
        //
//        Transaction oldTransaction = transactionService.findDuplicationTransaction(transactionId);
//        if(oldTransaction != null) {
//            LOGGER.error(Utility.prefixTxnId(transactionId,"processAuthRequest - found duplicate transaction : "+transactionId));
//            transaction = oldTransaction;
//            timeoutService.removeTimeoutTransaction(transactionId);
//            throw new ACSException(ExceptionConstant.DUPLICATE_TRANSACTION_REQUEST.getErrorCode(), ExceptionConstant.DUPLICATE_TRANSACTION_REQUEST.getErrorDesc());
//        }
        // check device channel
//        DeviceChannel channel = DeviceChannel.getDeviceChannel(reqAReq.getDeviceChannel());
//        if(null == channel){
//            transaction.setTransactionStatus(TransactionStatus.UNABLE_TO_AUTHENTICATE);
//            transaction.setTransactionStatusReason(TransactionStatusReason.UNSUPPORTED_DEVICE.getCode());
//            throw new ACSException(TransactionStatusReason.UNSUPPORTED_DEVICE.getCode(),TransactionStatusReason.UNSUPPORTED_DEVICE.getDesc());
//        }



        // Check if instrument is valid active
        // range active
        // institution_acs_url CREATE table  check url exist
        //  check if card number exist and valid
//        User user = null;
//        try {
//            user = userDetailService.findByUserId(transaction, transaction.getCardNumber());
//        } catch(ACSDataAccessException acsException) {
//            String errorMessage = "Error Code: " + acsException.getMessage();
//            LOGGER.error("Error while getting card details : "+errorMessage, acsException.getMessage());
//
//            if(transaction.getNetworkId().equals(Network.MASTERCARD.getValue())) {
//                transaction.setTransactionStatus(TransactionStatus.REJECTED);
//            }else {
//                transaction.setTransactionStatus(TransactionStatus.FAILED);
//            }
//
//            transaction.setTransactionStatusReason(TransactionStatusReason.NO_CARD_RECORD.getCode());
//            transactionVO.setAcsReferenceNumber(environment.getProperty(Constant.ACS_REFERENCE_NUMBER));
//            transactionVO.setMessageVersion(reqAReq.getMessageVersion());
//            throw acsException;
//            //throw new ACSException(dataNotFoundException.getErrorCode(), dataNotFoundException.getMessage());
//        }


    }
}
