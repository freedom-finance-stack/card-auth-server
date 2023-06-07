package com.razorpay.threeds.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static com.razorpay.threeds.constant.ThreeDSConstant.MESSAGE_TYPE_ERRO;
import static com.razorpay.threeds.constant.ThreeDSConstant.MESSAGE_TYPE_VERSION;


@Getter
@NoArgsConstructor
public class ThreeDSErrorResponse {

    //All Required Fields

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorComponent")
    private String errorComponent;

    @JsonProperty("errorDescription")
    private String errorDescription;

    @JsonProperty("errorDetail")
    private String errorDetail;

    @JsonProperty("messageType")
    private String messageType = MESSAGE_TYPE_ERRO;

    @JsonProperty("messageVersion")
    private String messageVersion = MESSAGE_TYPE_VERSION;

    //All Conditional Fields
    @JsonProperty("threeDSServerTransID")
    private String threeDSServerTransID;

    @JsonProperty("acsTransID")
    private String acsTransID;

    @JsonProperty("dsTransID")
    private String dsTransID;

    @JsonProperty("errorMessageType")
    private String errorMessageType;

    @JsonProperty("sdkTransID")
    private String sdkTransID;

    //-----------------------------

    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonIgnore
    private Instant timestamp = Instant.now();


    public ThreeDSErrorResponse(final HttpStatus status, final String errorCode, final String errorDetail, final String errorComponent, final String errorDescription) {

        this.httpStatus = status;
        this.errorCode = errorCode;
        this.errorComponent = errorComponent;
        this.errorDescription = errorDescription;
        this.errorDetail = errorDetail;
    }

}