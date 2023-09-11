package org.freedomfinancestack.razorpay.cas.acs.controller;

import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CVReq;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.InvalidStateTransactionException;
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
        CdRes cdRes = null;
        try {
            cdRes = challengeRequestService.processBrwChallengeRequest(strCReq, threeDSSessionData);
        } catch (ACSDataAccessException | InvalidStateTransactionException e) {
            // todo unhandled exception and if challengeResponse is null
            throw new RuntimeException(e);
        }
        if (cdRes.isError()) {
            return createCresAndErrorMessageResponse(model, cdRes);
        }
        model.addAttribute("challengeResponse", cdRes);
        return "acsOtp";
    }

    private static String createCresAndErrorMessageResponse(Model model, CdRes cdRes) {
        if (Util.isNullorBlank(cdRes.getNotificationUrl())) {
            throw new RuntimeException("Transaction not recognized");
        }
        if (!Util.isNullorBlank(cdRes.getEncryptedCRes())) {
            model.addAttribute("cRes", cdRes.getEncryptedCRes());
        } else {
            model.addAttribute("erro", cdRes.getEncryptedErro());
        }
        model.addAttribute("notificationUrl", cdRes.getNotificationUrl());
        return "threeDSecureResponseSubmit";
    }

    /**
     * Handles Challenge Validation Request (ValidateCReq) received from the client browser for OTP
     * verification and generates CRes for the Browser.
     *
     * @param CVReq The {@link CVReq} object representing the * Challenge Validation Request message
     *     received from the browser.
     * @param model The {@link Model} object representing the UI Model for HTML template data
     *     binding.
     * @return The {@link String} object representing the Challenge Response message in JS enabled
     *     HTML format.
     */
    @RequestMapping(
            value = "/challenge/browser/validate",
            method = RequestMethod.GET,
            produces = "html/text;charset=utf-8",
            consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String handleChallengeValidationRequest(CVReq CVReq, Model model) {
        CdRes cdRes = challengeRequestService.processBrwChallengeValidationRequest(CVReq);
        if (cdRes.isChallengeCompleted() || cdRes.isError()) {
            return createCresAndErrorMessageResponse(model, cdRes);
        }
        model.addAttribute("challengeResponse", cdRes);
        return "acsOtp";
    }
}
