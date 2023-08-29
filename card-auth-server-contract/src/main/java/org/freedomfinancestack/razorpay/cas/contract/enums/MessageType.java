package org.freedomfinancestack.razorpay.cas.contract.enums;

public enum MessageType {
    AReq("AReq"),
    ARes("ARes"),
    CReq("CReq"),
    CRes("CRes"),
    Erro("Erro"),
    RReq("RReq"),
    CDRes("CDRes"),
    CVReq("CVReq"),
    RRes("RRes");

    private final String stringValue;

    MessageType(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
    // Method to fetch MessageType enum from a string value
    public static MessageType fromStringValue(String value) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.stringValue.equalsIgnoreCase(value)) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("No matching MessageType for value: " + value);
    }
}
