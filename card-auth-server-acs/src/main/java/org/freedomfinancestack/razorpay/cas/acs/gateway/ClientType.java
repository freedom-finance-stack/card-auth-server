package org.freedomfinancestack.razorpay.cas.acs.gateway;

public enum ClientType {
    VISA_DS("visaDs"),
    MASTERCARD_DS("mcDs");

    private final String serviceName;

    ClientType(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
