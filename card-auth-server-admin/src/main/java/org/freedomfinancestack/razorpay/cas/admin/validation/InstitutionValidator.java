package org.freedomfinancestack.razorpay.cas.admin.validation;

import org.freedomfinancestack.razorpay.cas.admin.dto.GetInstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionRequestDto;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "institutionValidator")
public class InstitutionValidator {

    public void validateCreateInstitutionRequest(
            @NonNull final InstitutionRequestDto institutionRequestDto) {}

    public void validateGetInstitutionRequest(
            @NonNull final GetInstitutionRequestDto getInstitutionRequestDto) {}

    public void validatePatchInstitutionRequest(
            @NonNull final InstitutionRequestDto institutionRequestDto) {}

    public void validateDeleteInstitutionRequest(@NonNull final String institutionId) {}
}
