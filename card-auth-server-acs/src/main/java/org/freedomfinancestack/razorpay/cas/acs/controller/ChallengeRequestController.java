package org.freedomfinancestack.razorpay.cas.acs.controller;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CVReq;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Handles browser Challenge Request generating user's browser")
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
    public String handleChallengeRequest(
            @RequestParam(name = "creq") String strCReq,
            @RequestParam(name = "threeDSSessionData", required = false) String threeDSSessionData,
            Model model) {
        CdRes cdRes =
                challengeRequestService.processBrwChallengeRequest(strCReq, threeDSSessionData);
        if (cdRes.isChallengeCompleted() || cdRes.isError()) {
            return createCresAndErrorMessageResponse(model, cdRes);
        }
        return createCdRes(model, cdRes);
    }

    private static String createCresAndErrorMessageResponse(Model model, CdRes cdRes) {
        if (Util.isNullorBlank(cdRes.getNotificationUrl())) {
            throw new RuntimeException("Transaction not recognized");
        }
        if (!Util.isNullorBlank(cdRes.getEncryptedErro())) {
            model.addAttribute(InternalConstants.MODEL_ATTRIBUTE_ERRO, cdRes.getEncryptedErro());
        } else {
            model.addAttribute(InternalConstants.MODEL_ATTRIBUTE_CRES, cdRes.getEncryptedCRes());
        }
        model.addAttribute(
                InternalConstants.MODEL_ATTRIBUTE_THREEDS_SESSION_DATA,
                cdRes.getThreeDSSessionData());
        model.addAttribute(
                InternalConstants.MODEL_ATTRIBUTE_NOTIFICATION_URL, cdRes.getNotificationUrl());
        return "threeDSecureResponseSubmit";
    }

    private static String createCdRes(Model model, CdRes cdRes) {
        model.addAttribute("cdRes", cdRes);
        return "acsOtp";
    }

    /**
     * Handles Challenge Validation Request (ValidateCReq) received from the client browser for OTP
     * verification and generates CRes for the Browser.
     *
     * @param cVReq The {@link CVReq} object representing the * Challenge Validation Request message
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
    @Operation(summary = "Handles browser validation Challenge Request generating user's browser")
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
    public String handleChallengeValidationRequest(CVReq cVReq, Model model) {
        CdRes cdRes = challengeRequestService.processBrwChallengeValidationRequest(cVReq);
        if (cdRes.isChallengeCompleted() || cdRes.isError()) {
            return createCresAndErrorMessageResponse(model, cdRes);
        }
        return createCdRes(model, cdRes);
    }
}
