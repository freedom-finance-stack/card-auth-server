package org.freedomfinancestack.razorpay.cas.dao.enums;

import java.util.Objects;

import org.freedomfinancestack.razorpay.cas.dao.statemachine.InvalidStateTransactionException;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.State;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Phase implements State<Phase.PhaseEvent> {
    AREQ {
        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            switch (event) {
                case AUTHORIZATION_PROCESSED:
                    return Phase.ARES;
                case ERROR_OCCURRED:
                    return Phase.AERROR;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    ARES {
        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            switch (event) {
                case CREQ_RECEIVED:
                    return Phase.CREQ;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    AERROR {
        @Override
        public Phase nextState(PhaseEvent event) {
            // final state can't move anywhere
            log.error("INVALID TRANSITION, AERROR IS FINAL STATE");
            return Phase.AERROR;
        }
    },
    CREQ {
        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            switch (event) {
                case SEND_AUTH_VAL:
                    return Phase.CDRES;
                case RESEND_CHALLENGE:
                    return Phase.CREQ;
                case TRANSACTION_FAILED:
                case CANCEL_CHALLENGE:
                case ERROR_OCCURRED:
                    return Phase.RREQ;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    CDRES { // challenge display response

        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            switch (event) {
                case VALIDATION_REQ_RECEIVED:
                    return Phase.CVREQ;
                case RESEND_CHALLENGE:
                    return Phase.CREQ;
                case CANCEL_CHALLENGE:
                case ERROR_OCCURRED:
                    return Phase.RREQ;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    CVREQ { // challenge validation request

        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            switch (event) {
                case INVALID_AUTH_VAL:
                    return Phase.CDRES;
                case RESEND_CHALLENGE:
                    return Phase.CREQ;
                case AUTH_VAL_VERIFIED:
                case AUTH_ATTEMPT_EXHAUSTED:
                case CANCEL_CHALLENGE:
                case ERROR_OCCURRED:
                    return Phase.RREQ;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    RREQ {
        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            switch (event) {
                case RREQ_FAILED:
                    return Phase.CRES;
                case RRES_RECEIVED:
                    return Phase.RRES;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    RRES {
        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            if (Objects.requireNonNull(event) == PhaseEvent.CHALLENGE_COMPLETED) {
                return Phase.CRES;
            }
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    },
    CRES {
        @Override
        public Phase nextState(PhaseEvent event) throws InvalidStateTransactionException {
            // final state can't move anywhere
            throw new InvalidStateTransactionException(this.toString(), event.toString());
        }
    };

    public enum PhaseEvent {
        AUTHORIZATION_PROCESSED,
        CREQ_RECEIVED,
        SEND_AUTH_VAL,
        RESEND_CHALLENGE,
        VALIDATION_REQ_RECEIVED,
        AUTH_ATTEMPT_EXHAUSTED,
        INVALID_AUTH_VAL,
        AUTH_VAL_VERIFIED,
        CANCEL_CHALLENGE,
        RREQ_FAILED,
        RRES_RECEIVED,
        CHALLENGE_COMPLETED,
        ERROR_OCCURRED,
        TRANSACTION_FAILED,
        CHALLENGE_DATA_ENTRY
    }
}
