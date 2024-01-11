package org.freedomfinancestack.razorpay.cas.acs.data;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.dao.enums.OOBType;

import static org.freedomfinancestack.razorpay.cas.acs.data.AuthConfigDtoData.createAuthConfigDto;
import static org.freedomfinancestack.razorpay.cas.acs.data.TransactionTestData.createSampleAppTransaction;

public class AuthenticationDtoData {
    public static AuthenticationDto createAuthenticationDto(AuthConfigDto authConfigDto){
        return AuthenticationDto.builder().
                authConfigDto(authConfigDto).
                transaction(createSampleAppTransaction()).
                authValue("")
                .build();
    }
}
