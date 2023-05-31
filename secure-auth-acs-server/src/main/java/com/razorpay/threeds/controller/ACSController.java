package com.razorpay.threeds.controller;

import com.razorpay.threeds.contract.AREQ;
import com.razorpay.threeds.contract.ARES;
import com.razorpay.threeds.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

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

    @PostMapping(
            value = "/auth-request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ARES handleAuthenticationRequest(
            @RequestBody @Valid AREQ areq,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        return authenticationService.processAuthenticationRequest(areq);
    }
}
