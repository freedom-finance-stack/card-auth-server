package org.freedomfinancestack.razorpay.cas.dao.statemachine;

public interface State<S, E> {
    S nextState(E event) throws InvalidStateTransactionException;
}
