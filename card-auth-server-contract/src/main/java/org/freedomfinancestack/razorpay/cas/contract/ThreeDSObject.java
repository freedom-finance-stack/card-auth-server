package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

public abstract class ThreeDSObject {
    public abstract MessageType getThreeDSMessageType();
}
