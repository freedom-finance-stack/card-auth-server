package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInstitutionResponseDto {
    private String institutionId;

    private String institutionName;

    private String shortName;

    private String messageVersion;

    private Short isoCountryCode;

    private String timezone;

    private boolean fetchMeta;
}
