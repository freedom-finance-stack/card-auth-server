package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.sql.Timestamp;

import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSObject;
import org.freedomfinancestack.razorpay.cas.dao.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageTypeDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.TransactionMessageTypeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the TransactionMessageTypeService interface that provides functionality to
 * create and save transaction message type details for AREQ and CREQ messages.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionMessageTypeServiceImpl implements TransactionMessageTypeService {

    private final TransactionMessageTypeDetailRepository transactionMessageTypeDetailRepository;

    public TransactionMessageTypeDetail create(AREQ areq, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail =
                new TransactionMessageTypeDetail(Util.toJson(areq), MessageType.AReq);
        transactionMessageTypeDetail.setId(Util.generateUUID());
        transactionMessageTypeDetail.setTransactionId(transactionId);
        transactionMessageTypeDetail.setReceivedTimestamp(
                new Timestamp(System.currentTimeMillis()));
        return transactionMessageTypeDetail;
    }

    public TransactionMessageTypeDetail create(CREQ creq, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail =
                new TransactionMessageTypeDetail(Util.toJson(creq), MessageType.CReq);
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
        TransactionMessageTypeDetail transactionMessageTypeDetail = null;
        if (threeDSObject instanceof AREQ) {
            transactionMessageTypeDetail = create((AREQ) threeDSObject, transactionId);
        } else if (threeDSObject instanceof CREQ) {
            transactionMessageTypeDetail = create((CREQ) threeDSObject, transactionId);
        }
        this.save(transactionMessageTypeDetail);
    }
}
