package org.freedomfinancestack.razorpay.cas.acs.dto;

import lombok.Data;

@Data
public class ChallengeFlowDto {
    CdRes cdRes;
    boolean sendRreq;
}
