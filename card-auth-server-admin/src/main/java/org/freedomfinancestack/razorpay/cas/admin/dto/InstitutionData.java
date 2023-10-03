package org.freedomfinancestack.razorpay.cas.admin.dto;

import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;

public class InstitutionData {
    public String id;
    public String institutionName;

    public String institutionShortName;

    public Short isoCountryCode;

    public String timezone;

    public String messageVersion;

    public InstitutionStatus status;
}
