package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThymeleafServiceImplTest {

    @Mock private TemplateEngine templateEngine;

    @InjectMocks private ThymeleafServiceImpl thymeleafService;

    @Test
    public void testConstructor() {
        ThymeleafServiceImpl thymeleafServiceImpl = new ThymeleafServiceImpl();
        assertNotNull(thymeleafServiceImpl);
    }

    @Test
    void testGetOtpHTMLPage() {

        InstitutionUIParams institutionUIParams = new InstitutionUIParams();
        String templateName = "testTemplate";

        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("acsOtp");

        String result = thymeleafService.getOtpHTMLPage(institutionUIParams, templateName);

        assertNotNull(result);
        assertEquals("acsOtp", result);
    }
}
