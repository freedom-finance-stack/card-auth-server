package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionRequestDto {

    private InstitutionData institutionData;

    private InstitutionMetaData institutionMetaData;
}
