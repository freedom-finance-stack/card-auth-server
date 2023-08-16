package org.freedomfinancestack.razorpay.cas.acs.validator;

import lombok.extern.slf4j.Slf4j;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "challengeRequestValidator")
public class ChallengeRequestValidator<T extends CREQ> {

    public void validateRequest(CREQ asd, AREQ areq, ARES ares) throws ThreeDSException {}
}
