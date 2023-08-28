package org.freedomfinancestack.razorpay.cas.acs.module.custom;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

// In Extensions Dependency
@Slf4j
public class SecurityModuleAWS {

    public void verifyRequest(HttpServletRequest httpServletRequest) {
        log.info("httpServletRequest: ", httpServletRequest);
    }
}
