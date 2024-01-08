package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.contract.ThreeDSObject;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageLog;

/**
 * The {@code TransactionMessageLogService} interface provides methods to interact with the
 * TransactionMessageLog data.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface TransactionMessageLogService {
    /**
     * Saves a TransactionMessageLog object to the data store.
     *
     * @param transactionMessageLog The TransactionMessageLog object to be saved.
     */
    void save(TransactionMessageLog transactionMessageLog);

    /**
     * Creates and saves a TransactionMessageLog object to the data store based on a ThreeDSObject
     * and transactionId.
     *
     * @param threeDSObject The ThreeDSObject to be used for creating the TransactionMessageLog.
     * @param transactionId The transaction ID to be associated with the TransactionMessageLog.
     */
    void createAndSave(ThreeDSObject threeDSObject, String transactionId);
}
