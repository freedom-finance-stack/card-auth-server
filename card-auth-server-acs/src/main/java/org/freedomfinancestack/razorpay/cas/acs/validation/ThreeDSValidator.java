package org.freedomfinancestack.razorpay.cas.acs.validation;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSObject;

/**
 * The {@code ThreeDSValidator} interface defines a contract for validating ThreeDSObjects.
 *
 * <p>Any class that implements this interface must provide an implementation for the {@code
 * validateRequest} method, which will be responsible for validating a ThreeDSObject.
 *
 * @param <T> The type of ThreeDSObject to be validated.
 * @version 1.0.0
 * @since 1.0.0
 * @author ankitchoudhary2209
 */
public interface ThreeDSValidator<T extends ThreeDSObject> {

    /**
     * Validates the ThreeDSObject.
     *
     * @param request The ThreeDSObject to be validated.
     * @throws ThreeDSException If the object fails validation.
     */
    void validateRequest(T request) throws ThreeDSException;
}
