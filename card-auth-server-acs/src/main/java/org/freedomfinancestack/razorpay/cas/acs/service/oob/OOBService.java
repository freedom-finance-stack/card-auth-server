package org.freedomfinancestack.razorpay.cas.acs.service.oob;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;

public interface OOBService {
    void preAuthenticate(AuthenticationDto authenticationDto) throws ThreeDSException;

    AuthResponse authenticate(AuthenticationDto authenticationDto) throws ThreeDSException;
}
