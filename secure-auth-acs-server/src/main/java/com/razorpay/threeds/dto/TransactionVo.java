package com.razorpay.threeds.dto;

import com.razorpay.acs.dao.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionVo {
//    private String id;
//    private String institutionId;
//    private MessageCategory messageCategory;
//    private String messageVersion;
//    private FlowType flowType;
//    private TransactionStatus transactionStatus;
//    private String transactionStatusReason;
//    private Phase phase;
//    private String threedsSessionData;
//    private String authValue;
//    private String deviceChannel;
//    private String deviceName;
//    private Integer interactionCount;
//    private String errorCode;
//    private Timestamp created_at;
//    private Timestamp modified_at;
    private Transaction transaction;
    private TransactionMerchant transactionMerchant;
    private TransactionCardDetail transactionCardDetail;
    private TransactionBrowserDetail transactionBrowserDetail;
    private TransactionReferenceDetail transactionReferenceDetail;
}