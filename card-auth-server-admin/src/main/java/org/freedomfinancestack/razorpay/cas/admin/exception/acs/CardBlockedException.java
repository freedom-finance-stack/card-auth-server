package org.freedomfinancestack.razorpay.cas.admin.exception.acs;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;

/**
 * This class represents exceptions specific to the ACS (Access Control Server) functionality. This
 * Exception will be handled in code and should not return Erro message type All exceptions related
 * to ACS processing and need not send Erro in response should extend this class.
 */
public class CardBlockedException extends ACSException {

    private static final long serialVersionUID = 1L;

    public CardBlockedException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    public CardBlockedException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }
}
