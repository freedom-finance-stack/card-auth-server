package com.razorpay.threeds.dto;

import com.razorpay.acs.dao.enums.FlowType;
import com.razorpay.acs.dao.enums.Phase;
import com.razorpay.acs.dao.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private String id;
    private String institutionId;
    private String messageCategory;
    private String messageVersion;
    private FlowType flowType;
    private TransactionStatus transactionStatus;
    private String transactionStatusReason;
    private Phase phase;
    private String threedsSessionData;
    private String authValue;
    private String deviceChannel;
    private String deviceName;
    private Integer interactionCount;
    private String errorCode;
}