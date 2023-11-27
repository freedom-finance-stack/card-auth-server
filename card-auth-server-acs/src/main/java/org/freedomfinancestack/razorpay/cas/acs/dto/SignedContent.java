package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.EphemPubKey;

import lombok.Data;

@Data
public class SignedContent {
    private EphemPubKey acsEphemPubKey;
    private EphemPubKey sdkEphemPubKey;
    private String acsURL;
}
