package org.freedomfinancestack.razorpay.cas.acs.controller;

import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationRequestService;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@link AuthenticationRequestController} class is a REST controller responsible for handling
 * Authentication Request {@link AREQ} from the 3DS Server. This endpoint supports 3DS version 2.x.x
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ankitchoudhary2209, jaydeepRadadiya
 */
@Slf4j
@RestController
@RequestMapping(RouteConstants.TRANSACTION_ROUTE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationRequestController {

    private final AuthenticationRequestService authenticationRequestService;

    /**
     * Handles Authentication Request (AReq) received from the 3DS Server and generates
     * Authentication Response (ARes).
     *
     * @param areq The {@link AREQ} object representing the Authentication Request message received
     *     from the 3DS Server.
     * @param httpServletRequest The {@link HttpServletRequest} object representing the HTTP
     *     request.
     * @param httpServletResponse The {@link HttpServletResponse} object representing the HTTP
     *     response.
     * @param headers The {@link HttpHeaders} representing the request headers.
     * @return The {@link ARES} object representing the Authentication Response message.
     * @throws ThreeDSException if an error occurs during the processing of the AReq, and a
     *     challenge is required.
     */
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
            value = RouteConstants.AUTHENTICATION_ROUTE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "acs.auth-request", longTask = true)
    public ARES handleAuthenticationRequest(
            @RequestBody @Valid AREQ areq,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader HttpHeaders headers)
            throws ThreeDSException {
        return authenticationRequestService.processAuthenticationRequest(areq);
    }
}
