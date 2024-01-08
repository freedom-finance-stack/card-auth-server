package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AppOtpHtmlParams;
import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;

public interface ThymeleafService {
    String getAppOtpHTML(AppOtpHtmlParams appOtpHtmlParams, String templateName);

    String getOtpHTMLPage(InstitutionUIParams institutionUIParams, String templateName);
}
