package com.razorpay.ffs.cas.acs.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.service.TransactionMessageTypeService;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.contract.AREQ;
import com.razorpay.ffs.cas.contract.CREQ;
import com.razorpay.ffs.cas.contract.ThreeDSObject;
import com.razorpay.ffs.cas.dao.enums.MessageType;
import com.razorpay.ffs.cas.dao.model.TransactionMessageTypeDetail;
import com.razorpay.ffs.cas.dao.repository.TransactionMessageTypeDetailRepository;

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

    public TransactionMessageTypeDetail createAndSave(
            ThreeDSObject threeDSObject, String transactionId) {
        TransactionMessageTypeDetail transactionMessageTypeDetail = null;
        if (threeDSObject instanceof AREQ) {
            transactionMessageTypeDetail = create((AREQ) threeDSObject, transactionId);
        } else if (threeDSObject instanceof CREQ) {
            transactionMessageTypeDetail = create((CREQ) threeDSObject, transactionId);
        }
        this.save(transactionMessageTypeDetail);
        return transactionMessageTypeDetail;
    }
}
