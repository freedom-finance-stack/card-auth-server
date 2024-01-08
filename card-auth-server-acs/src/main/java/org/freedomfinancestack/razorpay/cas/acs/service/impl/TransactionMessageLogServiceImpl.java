package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageLogService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.*;
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
        if (threeDSObject == null) {
            return null;
        }
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
}
