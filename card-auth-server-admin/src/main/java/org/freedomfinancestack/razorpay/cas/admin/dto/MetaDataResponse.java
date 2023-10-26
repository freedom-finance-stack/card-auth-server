package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaDataResponse {
    private String[] supportedMessageVersions;

    private Short[] isoCountryCode;

    private String[] supportedTimezone;
}
