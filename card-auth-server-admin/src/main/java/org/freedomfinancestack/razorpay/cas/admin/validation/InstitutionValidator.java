package org.freedomfinancestack.razorpay.cas.admin.validation;

import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionRequestDto;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "institutionValidator")
public class InstitutionValidator {

    public void validateInstitutionRequest(
            @NonNull final InstitutionRequestDto institutionRequestDto) {}
}
