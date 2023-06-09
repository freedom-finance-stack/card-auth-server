package com.razorpay.threeds.controller;

import com.razorpay.threeds.configuration.ApplicationConfiguration;
import com.razorpay.threeds.contract.AREQ;
import com.razorpay.threeds.contract.ARES;
import com.razorpay.threeds.service.AuthenticationService;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController("acsController")
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSController {

    private final AuthenticationService authenticationService;
    private final ApplicationConfiguration applicationConfiguration;
    @PostMapping(
            value = "/auth-request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "acs.auth-request", longTask = true)
    public ARES handleAuthenticationRequest(
            @RequestBody @Valid AREQ areq,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        ApplicationConfiguration config = applicationConfiguration;
        return authenticationService.processAuthenticationRequest(areq);
    }
}
