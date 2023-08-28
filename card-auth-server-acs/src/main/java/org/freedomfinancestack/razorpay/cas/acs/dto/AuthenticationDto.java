package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationDto {
    private Transaction transaction;
    private AuthConfigDto authConfigDto;
    private String authValue;
}
