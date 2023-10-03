package org.freedomfinancestack.razorpay.cas.admin.service;

import org.freedomfinancestack.razorpay.cas.admin.dto.*;

public interface InstitutionService {

    DefaultSuccessResponse processCreateInstitutionOperation(
            InstitutionRequestDto institutionRequestDto);

    GetInstitutionResponseDto processGetInstitutionOperation(
            GetInstitutionRequestDto getInstitutionRequestDto);

    DefaultSuccessResponse processPatchInstitutionOperation(
            InstitutionRequestDto institutionRequestDto);

    DefaultSuccessResponse processDeleteInstitutionOperation(String institutionId);
}
