package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.InstitutionUIParams;

public interface ThymeleafService {
    String getOtpHTMLPage(InstitutionUIParams institutionUIParams, String templateName);
}
