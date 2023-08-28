package org.freedomfinancestack.razorpay.cas.dao.statemachine;

public class StateMachine {

    public static <S extends State<S, E>, E> void Trigger(StateMachineEntity<S, E> entity, E event)
            throws InvalidStateTransactionException {
        S state = entity.GetState();
        S nextState = state.nextState(event);
        if (nextState != null) {
            entity.SetState(nextState);
        }
    }
}
