package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.contract.ThreeDSObject;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionMessageTypeDetail;

/**
 * The {@code TransactionMessageTypeService} interface provides methods to interact with the
 * TransactionMessageTypeDetail data.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface TransactionMessageTypeService {
    /**
     * Saves a TransactionMessageTypeDetail object to the data store.
     *
     * @param transactionMessageTypeDetail The TransactionMessageTypeDetail object to be saved.
     */
    void save(TransactionMessageTypeDetail transactionMessageTypeDetail);

    /**
     * Creates and saves a TransactionMessageTypeDetail object to the data store based on a
     * ThreeDSObject and transactionId.
     *
     * @param threeDSObject The ThreeDSObject to be used for creating the
     *     TransactionMessageTypeDetail.
     * @param transactionId The transaction ID to be associated with the
     *     TransactionMessageTypeDetail.
     */
    void createAndSave(ThreeDSObject threeDSObject, String transactionId);
}
