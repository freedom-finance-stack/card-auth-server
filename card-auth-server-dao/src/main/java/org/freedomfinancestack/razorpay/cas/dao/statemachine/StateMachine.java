package org.freedomfinancestack.razorpay.cas.dao.statemachine;

public class StateMachine {

    public static <E> void Trigger(StateMachineEntity<E> entity, E event)
            throws InvalidStateTransactionException {
        State<E> state = entity.GetState();
        State<E> nextState = state.nextState(event);
        if (nextState != null) {
            entity.SetState(nextState);
        }
    }
}
