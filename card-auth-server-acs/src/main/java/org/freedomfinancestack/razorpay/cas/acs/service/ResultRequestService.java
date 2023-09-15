package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DSConnectionException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

/**
 * The {@code ResultRequestService} interface provides methods to send Result Requests to the DS.
 * The interface is implemented by the {@link
 * org.freedomfinancestack.razorpay.cas.acs.service.impl.ResultRequestServiceImpl}
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface ResultRequestService {

    /**
     * Saves or updates a Transaction object in the database and returns the transaction with the
     * current Hibernate session.
     *
     * @param transaction The Transaction object
     * @throws DSConnectionException If there is an error while connecting to the data store.
     * @throws ValidationException If the transaction is not valid.
     */
    void sendRreq(Transaction transaction) throws DSConnectionException, ValidationException;
}
