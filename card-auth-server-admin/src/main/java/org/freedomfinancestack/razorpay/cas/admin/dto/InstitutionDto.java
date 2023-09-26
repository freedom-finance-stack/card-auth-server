package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDto {
    private String institutionId;

    private String institutionName;

    private String institutionShortName;

    private Short isoCountryCode;

    private String timezone;
}

