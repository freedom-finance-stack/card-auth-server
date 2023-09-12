package org.freedomfinancestack.razorpay.cas.dao.statemachine;

public interface State<E> {
    State<E> nextState(E event) throws InvalidStateTransactionException;
}
