package org.freedomfinancestack.razorpay.cas.dao.statemachine;

import lombok.NonNull;

public class InvalidStateTransactionException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidStateTransactionException(
            @NonNull final String message, @NonNull final Throwable cause) {
        super(message, cause);
    }

    public InvalidStateTransactionException(@NonNull final String message) {
        super(message);
    }

    public InvalidStateTransactionException(
            @NonNull final String fromState, @NonNull final String event) {
        super("Invalid state transition from " + fromState + " using event " + event);
    }
}
