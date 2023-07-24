package com.razorpay.ffs.cas.acs.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.constant.InternalConstants;
import com.razorpay.ffs.cas.acs.constant.ThreeDSConstant;
import com.razorpay.ffs.cas.acs.exception.InternalErrorCode;
import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.service.TransactionService;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.contract.AREQ;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;
import com.razorpay.ffs.cas.contract.enums.DeviceChannel;
import com.razorpay.ffs.cas.contract.enums.MessageCategory;
import com.razorpay.ffs.cas.dao.enums.Phase;
import com.razorpay.ffs.cas.dao.enums.TransactionStatus;
import com.razorpay.ffs.cas.dao.model.Transaction;
import com.razorpay.ffs.cas.dao.model.TransactionBrowserDetail;
import com.razorpay.ffs.cas.dao.model.TransactionCardDetail;
import com.razorpay.ffs.cas.dao.model.TransactionMerchant;
import com.razorpay.ffs.cas.dao.model.TransactionPurchaseDetail;
import com.razorpay.ffs.cas.dao.model.TransactionReferenceDetail;
import com.razorpay.ffs.cas.dao.repository.TransactionReferenceDetailRepository;
import com.razorpay.ffs.cas.dao.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.razorpay.ffs.cas.contract.constants.EMVCOConstant.appDeviceInfoAndroid;
import static com.razorpay.ffs.cas.contract.constants.EMVCOConstant.appDeviceInfoIOS;
import static com.razorpay.ffs.cas.contract.utils.Util.DATE_FORMAT_YYYYMMDDHHMMSS;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionReferenceDetailRepository transactionReferenceDetailRepository;

    public Transaction saveOrUpdate(Transaction transaction) throws ACSDataAccessException {
        try {
            transactionRepository.save(transaction);
            return transactionRepository.findById(transaction.getId()).get();
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
        Timestamp time = null;
        try {
            time = Util.getTimeStampFromString(areq.getPurchaseDate(), DATE_FORMAT_YYYYMMDDHHMMSS);
        } catch (ParseException e) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                    String.format("Invalid PurchaseDate"));
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

    public Transaction findDuplicationTransaction(String threedsServerTransactionId) {
        List<TransactionReferenceDetail> transactionReferenceDetails =
                transactionReferenceDetailRepository.findByThreedsServerTransactionId(
                        threedsServerTransactionId);
        if (transactionReferenceDetails.size() != 0) {
            return transactionReferenceDetails.get(0).getTransaction();
        }
        return null;
    }
}
