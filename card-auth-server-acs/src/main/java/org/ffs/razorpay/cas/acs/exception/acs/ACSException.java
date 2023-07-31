package org.ffs.razorpay.cas.acs.exception.acs;

import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;

// This class represents exceptions specific to the ACS (Access Control Server) functionality.
// This Exception will be handled in code and should not return Erro message type
// All exceptions related to ACS processing and need not send Erro in response should extend this
// class.
public class ACSException extends Exception {

    /** */
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
