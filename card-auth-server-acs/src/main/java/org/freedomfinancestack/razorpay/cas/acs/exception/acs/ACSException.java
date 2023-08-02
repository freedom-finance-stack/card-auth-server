package org.freedomfinancestack.razorpay.cas.acs.exception.acs;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;

/**
 * This class represents exceptions specific to the ACS (Access Control Server) functionality. These
 * exceptions will be handled in the code and should not return "Erro" message type in the response.
 * All exceptions related to ACS processing that do not need to send "Erro" in the response should
 * extend this class.
 *
 * <p>The {@code ACSException} class is a custom exception that provides an internal error code and
 * an associated error message. It allows developers to encapsulate errors specific to ACS
 * processing and propagate them to higher levels of the application for proper handling.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * try {
 *     // Some ACS processing code that may throw an ACSException
 *     // ...
 * } catch (ACSException e) {
 *     // Handle the ACS-specific exception here
 *     // Log the error, return an error response, etc.
 * }
 * }</pre>
 *
 * <p>Note: The error message should be human-readable and provide enough information for developers
 * to understand the nature of the exception.
 *
 * @see InternalErrorCode
 * @version 1.0.0
 * @since ACS 1.0.0
 * @author jaydeepRadadiya
 */
public class ACSException extends Exception {

    private static final long serialVersionUID = -3009383961775806469L;

    private InternalErrorCode internalErrorCode;

    public ACSException(InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.internalErrorCode = internalErrorCode;
    }

    public ACSException(InternalErrorCode internalErrorCode, String message) {
        super(message);
        this.internalErrorCode = internalErrorCode;
    }

    public ACSException(InternalErrorCode internalErrorCode, Throwable cause) {
        super(internalErrorCode.getDefaultErrorMessage(), cause);
        this.internalErrorCode = internalErrorCode;
    }

    public ACSException(InternalErrorCode internalErrorCode) {
        super(internalErrorCode.getDefaultErrorMessage());
        this.internalErrorCode = internalErrorCode;
    }

    public InternalErrorCode getErrorCode() {
        return internalErrorCode;
    }
}
