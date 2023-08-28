package org.freedomfinancestack.razorpay.cas.dao.statemachine;

public interface StateMachineEntity<S extends State<S, E>, E> {

    String EntityName();

    void SetState(State<S, E> name);

    S GetState();
}
