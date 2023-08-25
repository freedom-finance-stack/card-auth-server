package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageTypeDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.TransactionMessageTypeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.dao.enums.Phase.CRES;

/**
 * Implementation of the TransactionMessageTypeService interface that provides functionality to
 * create and save transaction message type details for AREQ and CREQ messages.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionMessageTypeServiceImpl implements TransactionMessageTypeService {

    private final TransactionMessageTypeDetailRepository transactionMessageTypeDetailRepository;

    public TransactionMessageTypeDetail create(ThreeDSObject message, String transactionId) {

        TransactionMessageTypeDetail transactionMessageTypeDetail =
                new TransactionMessageTypeDetail(
                        Util.toJson(message), message.getThreeDSMessageType());
        transactionMessageTypeDetail.setId(Util.generateUUID());
        transactionMessageTypeDetail.setTransactionId(transactionId);
        transactionMessageTypeDetail.setReceivedTimestamp(
                new Timestamp(System.currentTimeMillis()));
        return transactionMessageTypeDetail;
    }

    public void save(TransactionMessageTypeDetail transactionMessageTypeDetail) {
        if (transactionMessageTypeDetail != null) {
            transactionMessageTypeDetailRepository.save(transactionMessageTypeDetail);
        }
    }

    public void createAndSave(ThreeDSObject threeDSObject, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail =
                create(threeDSObject, transactionId);
        this.save(transactionMessageTypeDetail);
    }

    /**
     * get all the messages for the transaction id and return a map of message for each message type
     *
     * @param id
     * @return
     * @throws ThreeDSException
     */
    @Override
    public Map<MessageType, ThreeDSObject> getTransactionMessagesByTransactionId(String id)
            throws ThreeDSException {
        Map<MessageType, ThreeDSObject> messageMap = new HashMap<>();
        List<TransactionMessageTypeDetail> messageTypeDetails =
                transactionMessageTypeDetailRepository.findAllByTransactionId(id);
        if (messageTypeDetails != null && !messageTypeDetails.isEmpty()) {
            return null;
        }
        for (TransactionMessageTypeDetail messageTypeDetail : messageTypeDetails) {
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
