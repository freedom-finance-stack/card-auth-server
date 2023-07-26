package org.ffs.razorpay.cas.acs.service.impl;

import java.sql.Timestamp;

import org.ffs.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.ffs.razorpay.cas.acs.utils.Util;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.contract.CREQ;
import org.ffs.razorpay.cas.contract.ThreeDSObject;
import org.ffs.razorpay.cas.dao.enums.MessageType;
import org.ffs.razorpay.cas.dao.model.TransactionMessageTypeDetail;
import org.ffs.razorpay.cas.dao.repository.TransactionMessageTypeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
