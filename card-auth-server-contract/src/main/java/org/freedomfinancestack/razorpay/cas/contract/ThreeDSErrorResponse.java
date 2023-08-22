package org.freedomfinancestack.razorpay.cas.contract;

import java.time.Instant;

import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThreeDSErrorResponse {

    // All Required Fields

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorComponent")
    private String errorComponent;

    @JsonProperty("errorDescription")
    private String errorDescription;

    @JsonProperty("errorDetail")
    private String errorDetail;

    @JsonProperty("errorMessageType")
    private String errorMessageType;

    @JsonProperty("messageType")
    private String messageType = MessageType.Erro.toString();

    @JsonProperty("messageVersion")
    private String messageVersion = EMVCOConstant.MESSAGE_TYPE_VERSION;

    // All Conditional Fields
    @JsonProperty("threeDSServerTransID")
    private String threeDSServerTransID;

    @JsonProperty("acsTransID")
    private String acsTransID;

    @JsonProperty("dsTransID")
    private String dsTransID;

    @JsonProperty("sdkTransID")
    private String sdkTransID;

    // -----------------------------

    @JsonIgnore private int httpStatus;

    @JsonIgnore private Instant timestamp = Instant.now();

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
