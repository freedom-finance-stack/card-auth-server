package com.razorpay.threeds.controller;

import com.razorpay.threeds.contract.AREQ;
import com.razorpay.threeds.contract.ARES;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController("acsController")
@RequestMapping("/v1/transaction")
public class ACSController {

    @PostMapping(value = "/auth-request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ARES processAuthenticationRequest(
            @RequestBody @Valid AREQ areq,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers) {
        ARES ares = new ARES();
        ares.setThreeDSServerTransID("Some-Guid");
        return ares;
    }
}
