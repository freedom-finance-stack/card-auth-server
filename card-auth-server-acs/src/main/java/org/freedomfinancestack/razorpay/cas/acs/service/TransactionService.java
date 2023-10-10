package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

/**
 * The {@code TransactionService} interface provides methods to interact with the Transaction data.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface TransactionService {

    /**
     * Saves or updates a Transaction object in the database and returns the transaction with the
     * current Hibernate session.
     *
     * @param transaction The Transaction object to be saved or updated.
     * @return The saved or updated Transaction object.
     * @throws ACSDataAccessException If there is an error while accessing the data store.
     */
    Transaction saveOrUpdate(Transaction transaction) throws ACSDataAccessException;

    /**
     * Creates a new Transaction object based on the provided AREQ (Authentication Request) and
     * returns it.
     *
     * @param areq The AREQ (Authentication Request) to be used for creating the Transaction.
     * @return The newly created Transaction object.
     * @throws ValidationException If the AREQ fails validation.
     */
    Transaction create(AREQ areq) throws ValidationException;

    /**
     * Finds a Transaction object by its ID in the database and returns it.
     *
     * @param id The ID of the Transaction to be found.
     * @return The found Transaction object, or null if no transaction is found with the specified
     *     ID.
     * @throws ACSDataAccessException If there is an error while accessing the data store.
     */
    Transaction findById(String id) throws ACSDataAccessException;
}
