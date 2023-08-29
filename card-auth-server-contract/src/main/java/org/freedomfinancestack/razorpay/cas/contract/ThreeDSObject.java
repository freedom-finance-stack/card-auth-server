package org.freedomfinancestack.razorpay.cas.contract;

import java.awt.*;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

// todo remove this class to ThreeDSMessage
public abstract class ThreeDSObject {
    public abstract MessageType getThreeDSMessageType();
}
