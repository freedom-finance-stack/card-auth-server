package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageLogService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageLog;
import org.freedomfinancestack.razorpay.cas.dao.repository.TransactionMessageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the TransactionMessageLogService interface that provides functionality to
 * create and save transaction message type details for AREQ and CREQ messages.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionMessageLogServiceImpl implements TransactionMessageLogService {

    private final TransactionMessageLogRepository transactionMessageLogRepository;

    public TransactionMessageLog create(ThreeDSObject threeDSObject, String transactionId) {

        TransactionMessageLog transactionMessageLog =
                new TransactionMessageLog(
                        Util.toJson(threeDSObject), threeDSObject.getThreeDSMessageType());
        transactionMessageLog.setId(Util.generateUUID());
        transactionMessageLog.setTransactionId(transactionId);
        return transactionMessageLog;
    }

    public void save(TransactionMessageLog transactionMessageLog) {
        if (transactionMessageLog != null) {
            transactionMessageLogRepository.save(transactionMessageLog);
        }
    }

    public void createAndSave(ThreeDSObject threeDSObject, String transactionId) {
        TransactionMessageLog transactionMessageLog = create(threeDSObject, transactionId);
        this.save(transactionMessageLog);
    }

    /**
     * get all the messages for the transaction id and return a map of message for each message type
     *
     * @param id transaction id for which messages are to be fetched.
     * @throws ThreeDSException
     * @return map with
     */
    @Override
    public Map<MessageType, ThreeDSObject> getTransactionMessagesByTransactionId(String id)
            throws ThreeDSException {
        Map<MessageType, ThreeDSObject> messageMap = new HashMap<>();
        // todo handle multiple entry exist for same type
        List<TransactionMessageLog> messageTypeDetails =
                transactionMessageLogRepository.findAllByTransactionId(id);
        if (messageTypeDetails == null || messageTypeDetails.isEmpty()) {
            return null;
        }
        for (TransactionMessageLog messageTypeDetail : messageTypeDetails) {
            String message = messageTypeDetail.getMessage();
            MessageType messageType = messageTypeDetail.getMessageType();
            switch (messageType) {
                case CRes:
                    messageMap.put(messageType, Util.fromJson(message, CRES.class));
                    break;
                case CReq:
                    messageMap.put(messageType, Util.fromJson(message, CREQ.class));
                    break;
                case ARes:
                    messageMap.put(messageType, Util.fromJson(message, ARES.class));
                    break;
                case AReq:
                    messageMap.put(messageType, Util.fromJson(message, AREQ.class));
                    break;
                case RReq:
                    messageMap.put(messageType, Util.fromJson(message, RREQ.class));
                    break;
                case CDRes:
                    messageMap.put(messageType, Util.fromJson(message, CdRes.class));
                    break;
                case CVReq:
                    messageMap.put(messageType, Util.fromJson(message, CVReq.class));
                    break;
                default:
                    throw new ThreeDSException(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            "Incorrect message data found in database");
            }
        }
        return messageMap;
    }
}
