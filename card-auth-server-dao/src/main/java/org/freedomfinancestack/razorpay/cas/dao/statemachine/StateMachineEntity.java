package org.freedomfinancestack.razorpay.cas.dao.statemachine;

public interface StateMachineEntity<E> {

    String EntityName();

    void SetState(State<E> name);

    State<E> GetState();
}
