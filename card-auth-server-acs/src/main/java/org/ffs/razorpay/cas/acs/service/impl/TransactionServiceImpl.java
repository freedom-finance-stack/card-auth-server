package org.ffs.razorpay.cas.acs.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import org.ffs.razorpay.cas.acs.constant.InternalConstants;
import org.ffs.razorpay.cas.acs.constant.ThreeDSConstant;
import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;
import org.ffs.razorpay.cas.acs.service.TransactionService;
import org.ffs.razorpay.cas.acs.utils.Util;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.ffs.razorpay.cas.contract.enums.DeviceChannel;
import org.ffs.razorpay.cas.contract.enums.MessageCategory;
import org.ffs.razorpay.cas.dao.enums.Phase;
import org.ffs.razorpay.cas.dao.enums.TransactionStatus;
import org.ffs.razorpay.cas.dao.model.Transaction;
import org.ffs.razorpay.cas.dao.model.TransactionBrowserDetail;
import org.ffs.razorpay.cas.dao.model.TransactionCardDetail;
import org.ffs.razorpay.cas.dao.model.TransactionMerchant;
import org.ffs.razorpay.cas.dao.model.TransactionPurchaseDetail;
import org.ffs.razorpay.cas.dao.model.TransactionReferenceDetail;
import org.ffs.razorpay.cas.dao.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.ffs.razorpay.cas.contract.constants.EMVCOConstant.appDeviceInfoAndroid;
import static org.ffs.razorpay.cas.contract.constants.EMVCOConstant.appDeviceInfoIOS;
import static org.ffs.razorpay.cas.contract.utils.Util.DATE_FORMAT_YYYYMMDDHHMMSS;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction saveOrUpdate(Transaction transaction) throws ACSDataAccessException {
        try {
            transactionRepository.save(transaction);
            return findById(transaction.getId());
        } catch (DataAccessException ex) {
            log.error("Error while saving transaction", ex);
            throw new ACSDataAccessException(InternalErrorCode.TRANSACTION_SAVE_EXCEPTION, ex);
        }
    }

    public Transaction findById(String id) throws ACSDataAccessException {
        try {
            Optional<Transaction> transaction = transactionRepository.findById(id);
            if (transaction.isPresent()) {
                return transaction.get();
            }
        } catch (DataAccessException ex) {
            throw new ACSDataAccessException(InternalErrorCode.TRANSACTION_FIND_EXCEPTION, ex);
        }

        return null;
    }

    public void remove(String id) {
        transactionRepository.softDeleteById(id);
    }

    public Transaction create(AREQ areq) throws ValidationException {
        Transaction transaction = createTransactionFromAreq(areq);
        transaction.setTransactionCardDetail(buildTransactionCardDetail(areq));
        transaction.setTransactionBrowserDetail(buildTransactionBrowserDetail(areq));
        transaction.setTransactionReferenceDetail(buildTransactionReferenceDetail(areq));
        transaction.setTransactionMerchant(buildTransactionMerchant(areq));
        transaction.setTransactionPurchaseDetail(buildTransactionPurchaseDetail(areq));
        return transaction;
    }

    private static Transaction createTransactionFromAreq(AREQ areq) {
        Transaction.TransactionBuilder transactionBuilder = Transaction.builder();
        transactionBuilder
                .id(areq.getTransactionId())
                .interactionCount(0)
                .phase(Phase.AREQ)
                .transactionStatus(TransactionStatus.CREATED);

        // Set default message version
        transactionBuilder.messageVersion(ThreeDSConstant.SUPPORTED_MESSAGE_VERSION[0]);
        Arrays.stream(ThreeDSConstant.SUPPORTED_MESSAGE_VERSION)
                .forEach(
                        availableVersion -> {
                            if (availableVersion.equals(areq.getMessageVersion())) {
                                transactionBuilder.messageVersion(areq.getMessageVersion());
                            }
                        });

        transactionBuilder.messageCategory(
                MessageCategory.getMessageCategory(areq.getMessageCategory()));
        String appDeviceInfo = "";
        if (areq.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            try {
                byte[] decodedByte = Base64.getDecoder().decode(areq.getDeviceInfo());
                JSONObject deviceInfoJson = new JSONObject(new String(decodedByte));
                JSONObject dd = deviceInfoJson.getJSONObject("DD");
                appDeviceInfo = dd.getString("C001");
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        if (areq.getDeviceChannel().equals(DeviceChannel.APP.getChannel())) {
            if (appDeviceInfo.equals(appDeviceInfoAndroid)) {
                transactionBuilder.deviceName(InternalConstants.ANDROID);
            } else if (appDeviceInfo.equals(appDeviceInfoIOS)) {
                transactionBuilder.deviceName(InternalConstants.IOS);
            }
            transactionBuilder.deviceChannel(DeviceChannel.APP.getChannel());
        } else if (areq.getDeviceChannel().equals(DeviceChannel.BRW.getChannel())) {
            transactionBuilder.deviceName(InternalConstants.BROWSER);
            transactionBuilder.deviceChannel(DeviceChannel.BRW.getChannel());
        }
        transactionBuilder.institutionId("institutionId");
        return transactionBuilder.build();
    }

    private TransactionPurchaseDetail buildTransactionPurchaseDetail(AREQ areq)
            throws ValidationException {
        Timestamp time;
        try {
            time = Util.getTimeStampFromString(areq.getPurchaseDate(), DATE_FORMAT_YYYYMMDDHHMMSS);
        } catch (ParseException e) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid PurchaseDate");
        }
        return TransactionPurchaseDetail.builder()
                .purchaseAmount(areq.getPurchaseAmount())
                .purchaseCurrency(areq.getPurchaseCurrency())
                .purchaseExponent(Byte.valueOf(areq.getPurchaseExponent()))
                .purchaseTimestamp(time)
                .payTokenInd(Boolean.valueOf(areq.getPayTokenInd()))
                .build();
    }

    private TransactionMerchant buildTransactionMerchant(AREQ areq) {
        return TransactionMerchant.builder()
                .acquirerMerchantId(areq.getAcquirerMerchantID())
                .merchantName(areq.getMerchantName())
                .merchantCountryCode(Short.valueOf(areq.getMerchantCountryCode()))
                .build();
    }

    private TransactionCardDetail buildTransactionCardDetail(AREQ areq) {
        return TransactionCardDetail.builder()
                .cardNumber(areq.getAcctNumber())
                .cardExpiry(areq.getCardExpiryDate())
                .cardholderName(areq.getCardholderName())
                .build();
    }

    private TransactionBrowserDetail buildTransactionBrowserDetail(AREQ areq) {
        return TransactionBrowserDetail.builder()
                .acceptHeader(areq.getBrowserAcceptHeader())
                .ip(areq.getBrowserIP())
                .javascriptEnabled(Boolean.valueOf(areq.getBrowserJavascriptEnabled()))
                .build();
    }

    private TransactionReferenceDetail buildTransactionReferenceDetail(AREQ areq) {
        return new TransactionReferenceDetail(
                areq.getThreeDSServerTransID(),
                areq.getThreeDSServerRefNumber(),
                areq.getDsTransID());
    }
}
