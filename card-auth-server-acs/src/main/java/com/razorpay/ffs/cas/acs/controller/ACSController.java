package com.razorpay.ffs.cas.acs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.razorpay.ffs.cas.acs.exception.ThreeDSException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSDataAccessException;
import com.razorpay.ffs.cas.acs.service.AuthenticationService;
import com.razorpay.ffs.cas.contract.AREQ;
import com.razorpay.ffs.cas.contract.ARES;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController("acsController")
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Handles Authentication Request generating from 3DS Server")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Request Successfully handled and validated"),
                @ApiResponse(
                        responseCode = "500",
                        description = "Server Exception Occurred during request handling"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request or Request not according to Areq Schema")
            })
    @PostMapping(
            value = "/auth-request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "acs.auth-request", longTask = true)
    public ARES handleAuthenticationRequest(
            @RequestBody @Valid AREQ areq,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers)
            throws ThreeDSException, ACSDataAccessException {
        return authenticationService.processAuthenticationRequest(areq);
    }
}
