package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.dto.AppOtpHtmlParams;

public interface ThymeleafService {
    String getAppOtpHTML(AppOtpHtmlParams appOtpHtmlParams, String templateName);
}
