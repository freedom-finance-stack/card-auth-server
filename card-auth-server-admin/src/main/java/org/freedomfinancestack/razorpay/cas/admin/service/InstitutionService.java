package org.freedomfinancestack.razorpay.cas.admin.service;

import org.freedomfinancestack.razorpay.cas.admin.dto.CreateInstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.CreateInstitutionResponseDto;
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

    public CreateInstitutionResponseDto processCreateInstitutionOperation(
            @NonNull final CreateInstitutionRequestDto createInstitutionRequestDto) {

        institutionValidator.validateInstitutionRequest(createInstitutionRequestDto);
        Institution institution =
                InstitutionMapper.INSTANCE.toInstitutionModel(createInstitutionRequestDto.getInstitutionDto());
        institutionRepository.save(institution);

        return CreateInstitutionResponseDto.builder().isSuccess(true).build();
    }
}
