package org.freedomfinancestack.razorpay.cas.admin.exception.acs;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;

/**
 * The {@code ACSDataAccessException} is a subclass of {@link ACSException} that represents
 * exceptions related to data access in the ACS (Access Control Server) functionality. These
 * exceptions indicate errors that occur during data retrieval or manipulation operations within the
 * ACS and should be handled appropriately in the code.
 *
 * <p>The {@code ACSDataAccessException} class extends {@link ACSException}, providing additional
 * functionality to handle data access errors with specific error codes and error messages.
 * Developers can use this exception to handle ACS-specific data access issues and propagate them to
 * higher levels of the application for proper handling and logging.
 *
 * @see ACSException
 * @see InternalErrorCode
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
public class ACSDataAccessException extends ACSException {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ACSDataAccessException} with the specified internal error code, error
     * message, and the cause of the exception.
     *
     * @param internalErrorCode The {@link InternalErrorCode} representing the internal error code
     *     of the exception.
     * @param message A {@link String} representing the error message associated with the exception.
     * @param cause The {@link Throwable} representing the cause of the exception.
     */
    public ACSDataAccessException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    public ACSDataAccessException(InternalErrorCode internalErrorCode, Throwable cause) {
        super(internalErrorCode, cause);
    }

    public ACSDataAccessException(InternalErrorCode internalErrorCode) {
        super(internalErrorCode);
    }

    public ACSDataAccessException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }
}
