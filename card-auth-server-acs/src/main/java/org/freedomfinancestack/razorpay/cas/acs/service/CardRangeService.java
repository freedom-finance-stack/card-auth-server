package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.CardDetailsNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;

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
public interface CardRangeService {
    /**
     * Finds and retrieves the CardRange entity based on the given primary key (rangeId).
     *
     * @param pan The car number key of the CardRange entity to be retrieved.
     * @return The {@link CardRange} entity corresponding to the given primary key.
     * @throws DataNotFoundException if the data entity is not found.
     * @throws ACSDataAccessException
     * @throws CardDetailsNotFoundException if the CardRange entity is not found.
     */
    CardRange findByPan(String pan)
            throws DataNotFoundException, ACSDataAccessException, CardDetailsNotFoundException;

    /**
     * Validates the CardRange entity based on the given transaction data.
     *
     * @param cardRange The {@link CardRange} entity to be validated.
     * @throws TransactionDataNotValidException if the transaction data entity is not valid.
     * @throws DataNotFoundException if the data entity is not found.
     * @throws CardDetailsNotFoundException if the CardRange entity is not found.
     */
    void validateRange(CardRange cardRange)
            throws TransactionDataNotValidException,
                    DataNotFoundException,
                    CardDetailsNotFoundException;
}
