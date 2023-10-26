package org.freedomfinancestack.razorpay.cas.admin.service;

import org.freedomfinancestack.razorpay.cas.admin.dto.*;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.GetInstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.InstitutionRequestDto;
import org.springframework.http.ResponseEntity;

public interface InstitutionService {

    ResponseEntity<?> processCreateInstitutionOperation(
            InstitutionRequestDto institutionRequestDto);

    ResponseEntity<?> processGetInstitutionOperation(
            GetInstitutionRequestDto getInstitutionRequestDto);

    ResponseEntity<?> processPatchInstitutionOperation(InstitutionRequestDto institutionRequestDto);

    ResponseEntity<?> processDeleteInstitutionOperation(String institutionId);
}
