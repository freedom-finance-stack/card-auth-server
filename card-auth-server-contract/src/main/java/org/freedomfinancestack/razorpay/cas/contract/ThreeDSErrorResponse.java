package org.freedomfinancestack.razorpay.cas.contract;

import java.time.Instant;

import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThreeDSErrorResponse {

    // All Required Fields
    private String errorCode;
    private String errorComponent;
    private String errorDescription;
    private String errorDetail;
    private String errorMessageType = MessageType.AReq.toString();
    private String messageType = MessageType.Erro.toString();
    private String messageVersion = EMVCOConstant.DEFAULT_MESSAGE_TYPE_VERSION;

    // All Conditional Fields
    private String threeDSServerTransID;
    private String acsTransID;
    private String dsTransID;
    private String sdkTransID;

    private transient int httpStatus;

    private transient Instant timestamp = Instant.now();

    public ThreeDSErrorResponse setAcsTransID(String acsTransID) {
        this.acsTransID = acsTransID;
        return this;
    }

    public ThreeDSErrorResponse setDsTransID(String dsTransID) {
        this.dsTransID = dsTransID;
        return this;
    }

    public ThreeDSErrorResponse setErrorMessageType(String errorMessageType) {
        this.errorMessageType = errorMessageType;
        return this;
    }

    public ThreeDSErrorResponse setSdkTransID(String sdkTransID) {
        this.sdkTransID = sdkTransID;
        return this;
    }

    public ThreeDSErrorResponse setThreeDSServerTransID(String threeDSServerTransID) {
        this.threeDSServerTransID = threeDSServerTransID;
        return this;
    }

    public ThreeDSErrorResponse setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ThreeDSErrorResponse(
            final int httpStatus,
            final String errorCode,
            final String errorComponent,
            final String errorDescription,
            final String errorDetail) {

        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorComponent = errorComponent;
        this.errorDescription = errorDescription;
        this.errorDetail = errorDetail;
    }
}
