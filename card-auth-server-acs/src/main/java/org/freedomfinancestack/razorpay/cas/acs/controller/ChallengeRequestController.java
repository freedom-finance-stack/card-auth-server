package org.freedomfinancestack.razorpay.cas.acs.controller;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.contract.CVReq;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

/**
 * The {@code ACSController} class is a Web View controller responsible for handling Challenge flow
 * from the 3DS Server.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor
public class ChallengeRequestController {

    private final ChallengeRequestService challengeRequestService;

    /**
     * Handles Challenge Request (CReq) received from the 3DS Server and generates HTML pages for
     * OTP or CRes for client browser.
     *
     * @param strCReq The {@link String} object representing the Challenge Request message received
     *     from the 3DS Server.
     * @param threeDSSessionData The {@link String} object representing the threeDSSessionData.
     * @param model The {@link Model} object representing the UI Model for HTML template data
     *     binding.
     * @return The {@link String} object representing the Challenge Response message in JS enabled
     *     HTML format.
     */
    @RequestMapping(
            value = "/challenge/browser",
            method = RequestMethod.POST,
            produces = "html/text;charset=utf-8",
            consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String handleChallengeRequest(
            @RequestParam(name = "creq") String strCReq,
            @RequestParam(name = "threeDSSessionData", required = false) String threeDSSessionData,
            Model model) {
        ChallengeResponse challengeResponse =
                challengeRequestService.processBrwChallengeRequest(strCReq, threeDSSessionData);
        // todo unhandled exception, if challengeResponse is null
        if (challengeResponse.isError()) {
            model.addAttribute("cRes", challengeResponse.getCRes());
            model.addAttribute("notificationUrl", challengeResponse.getNotificationUrl());
            return "threeDSecureResponseSubmit";
        }
        model.addAttribute("challengeResponse", challengeResponse);
        return "acsOtp";
    }

    /**
     * Handles Challenge Validation Request (ValidateCReq) received from the client browser for OTP
     * verification and generates CRes for the Browser.
     *
     * @param CVReq
     * @param model
     * @return
     */
    @RequestMapping(
            value = "/challenge/browser/validate",
            method = RequestMethod.GET,
            produces = "html/text;charset=utf-8",
            consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String handleChallengeValidationRequest(CVReq CVReq, Model model) {
        ValidateChallengeResponse validateChallengeResponse =
                challengeRequestService.processBrwChallengeValidationRequest(CVReq);
        model.addAttribute("cRes", validateChallengeResponse.getCRes());
        model.addAttribute("notificationUrl", validateChallengeResponse.getNotificationUrl());
        model.addAttribute("threeDSSessionData", validateChallengeResponse.getThreeDSSessionData());
        return "threeDSecureResponseSubmit";
    }
}
