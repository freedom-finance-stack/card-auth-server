package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInstitutionRequestDto {
    private String institution_id;

    private boolean fetch_meta;
}
