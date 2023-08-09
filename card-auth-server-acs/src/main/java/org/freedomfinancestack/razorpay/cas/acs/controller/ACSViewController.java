package org.freedomfinancestack.razorpay.cas.acs.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationService;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * The {@code ACSController} class is a Web View controller responsible for handling Challenge Request (Creq) from the 3DS Server.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
//@RestController("acsController")
@Controller
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ACSViewController {

    private final AuthenticationService authenticationService;

    @RequestMapping(value = "/challenge-request", method = RequestMethod.POST, produces = "application/json; charset=utf-8", consumes ="application/x-www-form-urlencoded;charset=UTF-8")
    public String handleChallengeRequest(@RequestParam(name = "creq") String strCReq, Model model) {
        model.addAttribute("message", strCReq);
        return "acsOtp";
    }

    @RequestMapping(value = "/challenge-validation-request", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public String handleChallengeValidationRequest( Model model) {
        model.addAttribute("message", "strCReq");
        return "acsOtp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String register(Model model) {
        System.out.println("register");
        return "";
    }
}
