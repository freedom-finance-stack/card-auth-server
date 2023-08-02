package org.ffs.razorpay.cas.acs.service;

import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.ffs.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.ffs.razorpay.cas.dao.model.CardRange;

/**
 * The {@code RangeService} interface provides methods to retrieve/process/validate/save CardRange
 * entities. The service is responsible for querying the data source to fetch the corresponding
 * CardRange entity based on the given primary key. The service also provides methods to validate
 * the CardRange entity based on the transaction data.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public interface RangeService {
    /**
     * Finds and retrieves the CardRange entity based on the given primary key (rangeId).
     *
     * @param pan
     * @return The {@link CardRange} entity corresponding to the given primary key.
     * @throws DataNotFoundException
     * @throws ACSDataAccessException
     */
    CardRange findByPan(String pan) throws DataNotFoundException, ACSDataAccessException;

    /**
     * Validates the CardRange entity based on the given transaction data.
     *
     * @param cardRange
     * @throws TransactionDataNotValidException
     * @throws DataNotFoundException
     */
    void validateRange(CardRange cardRange)
            throws TransactionDataNotValidException, DataNotFoundException;
}
