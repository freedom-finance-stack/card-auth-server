package org.freedomfinancestack.razorpay.cas.admin.service;

import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionResponseDto;
import org.freedomfinancestack.razorpay.cas.admin.mapper.InstitutionMapper;
import org.freedomfinancestack.razorpay.cas.admin.validation.InstitutionValidator;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("institutionService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionService {

    private final InstitutionValidator institutionValidator;

    private final InstitutionRepository institutionRepository;

    public InstitutionResponseDto processCreateInstitutionOperation(
            @NonNull final InstitutionRequestDto institutionRequestDto) {

        institutionValidator.validateInstitutionRequest(institutionRequestDto);
        Institution institution =
                InstitutionMapper.INSTANCE.toInstitutionModel(institutionRequestDto);
        institutionRepository.save(institution);

        return InstitutionResponseDto.builder().isSuccess(true).build();
    }
}
