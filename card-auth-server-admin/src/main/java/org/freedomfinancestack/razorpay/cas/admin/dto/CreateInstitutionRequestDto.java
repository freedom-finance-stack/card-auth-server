package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstitutionRequestDto {

    //    private String institutionId;

    private String institutionName;

    private String institutionShortName;

    private Short isoCountryCode;

    private String timezone;

    private String messageVersion;

    private InstitutionMeta institutionMeta;

    //    private InstitutionDto institutionDto;

    //    private InstitutionMetaDto institutionMetaDto;
}
