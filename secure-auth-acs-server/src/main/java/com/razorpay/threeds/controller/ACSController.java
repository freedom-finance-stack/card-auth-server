package com.razorpay.threeds.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.razorpay.acs.dao.contract.AREQ;
import com.razorpay.acs.dao.contract.ARES;
import com.razorpay.threeds.exception.ThreeDSException;
import com.razorpay.threeds.service.AuthenticationService;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

@RestController("acsController")
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSController {

  private final AuthenticationService authenticationService;

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
      throws ThreeDSException {
    return authenticationService.processAuthenticationRequest(areq);
  }
}
