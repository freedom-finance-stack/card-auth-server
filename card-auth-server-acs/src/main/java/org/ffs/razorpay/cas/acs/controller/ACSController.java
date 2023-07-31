package org.ffs.razorpay.cas.acs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.ffs.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.ffs.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.ffs.razorpay.cas.acs.service.AuthenticationService;
import org.ffs.razorpay.cas.contract.AREQ;
import org.ffs.razorpay.cas.contract.ARES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
