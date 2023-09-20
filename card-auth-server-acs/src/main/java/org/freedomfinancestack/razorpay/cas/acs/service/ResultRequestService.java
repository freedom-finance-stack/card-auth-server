package org.freedomfinancestack.razorpay.cas.acs.service;

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
     */
    void processRreq(Transaction transaction);
}
