package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.nio.charset.StandardCharsets;

import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.freedomfinancestack.razorpay.cas.acs.service.ThymeleafService;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThymeleafServiceImpl implements ThymeleafService {

    private final TemplateEngine templateEngine;

    public ThymeleafServiceImpl() {
        this.templateEngine = initializeTemplateEngine();
    }

    private TemplateEngine initializeTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setTemplateMode(TemplateMode.HTML);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    @Override
    public String getOtpHTMLPage(InstitutionUIParams institutionUIParams, String templateName) {

        CREQ creq = new CREQ();

        // Create Thymeleaf context
        Context context = new Context();
        context.setVariable("institutionUIParams", institutionUIParams);
        context.setVariable("cReq", creq);

        // Process Thymeleaf template
        return templateEngine.process(templateName, context);
    }

    public static void main(String[] args) {
        InstitutionUIParams institutionUIParams = new InstitutionUIParams();
        institutionUIParams.setAmount("10000");
        institutionUIParams.setCurrency("USD");
        institutionUIParams.setChallengeInfoHeader("OTP Verification");
        institutionUIParams.setChallengeInfoLabel("Enter OTP");
        institutionUIParams.setChallengeInfoText("challenge info text");
        //        institutionUIParams.setOtpAttemptLeft("2");
        institutionUIParams.setResendAttemptLeft("1");
        institutionUIParams.setResendBlocked("false");
        institutionUIParams.setResendInformationLabel("Resend OTP");
        institutionUIParams.setMerchantName("ICICI BANK");
        institutionUIParams.setCardNumber("1234XXXXXXXX5678");
        institutionUIParams.setOtpLength(6);
        institutionUIParams.setJSEnabled(true);
        institutionUIParams.setTimeout(180);
        institutionUIParams.setDeviceChannel("01");
        institutionUIParams.setAcsTransID("12345678");
        institutionUIParams.setThreeDSServerTransID("12345678");
        institutionUIParams.setMessageVersion("2.2.0");
        institutionUIParams.setSubmitAuthenticationLabel("Submit");
        institutionUIParams.setWhyInfoLabel("setWhyInfoLabel");
        institutionUIParams.setWhyInfoText("setWhyInfoText");
        institutionUIParams.setExpandInfoLabel("setExpandInfoLabel");
        institutionUIParams.setExpandInfoText("setExpandInfoText");

        CREQ creq = new CREQ();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setTemplateMode(TemplateMode.HTML);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("institutionUIParams", institutionUIParams);
        context.setVariable("cReq", creq);

        String html = templateEngine.process("acsOtpNew", context);

        log.info(html);
    }
}
