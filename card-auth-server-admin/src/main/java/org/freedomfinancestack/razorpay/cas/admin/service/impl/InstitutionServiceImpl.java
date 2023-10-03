package org.freedomfinancestack.razorpay.cas.admin.service.impl;

import java.sql.Timestamp;
import javax.transaction.Transactional;

import org.freedomfinancestack.razorpay.cas.admin.constants.InternalConstants;
import org.freedomfinancestack.razorpay.cas.admin.dto.*;
import org.freedomfinancestack.razorpay.cas.admin.mapper.InstitutionMapper;
import org.freedomfinancestack.razorpay.cas.admin.service.InstitutionService;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.InstitutionValidator;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("InstitutionServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionServiceImpl implements InstitutionService {
    private final InstitutionValidator institutionValidator;

    private final InstitutionRepository institutionRepository;

    @Override
    public DefaultSuccessResponse processCreateInstitutionOperation(
            @NonNull final InstitutionRequestDto institutionRequestDto) {

        institutionValidator.validateCreateInstitutionRequest(institutionRequestDto);

        Institution institution =
                InstitutionMapper.INSTANCE.toInstitutionModel(institutionRequestDto);

        String institutionId = Util.generateRandomStringID(InternalConstants.DEFAULT_LENGTH);

        institution.setId(institutionId);

        if (institution.getInstitutionMeta() != null) {
            institution.getInstitutionMeta().setInstitution(institution);
        }

        institutionRepository.save(institution);

        return DefaultSuccessResponse.builder().isSuccess(true).build();
    }

    @Override
    @Transactional
    public GetInstitutionResponseDto processGetInstitutionOperation(
            @NonNull final GetInstitutionRequestDto getInstitutionRequestDto) {

        institutionValidator.validateGetInstitutionRequest(getInstitutionRequestDto);

        Institution institution =
                institutionRepository
                        .findById(getInstitutionRequestDto.getInstitution_id())
                        .orElse(null);

        return InstitutionMapper.INSTANCE.toInstitutionDto(
                institution, getInstitutionRequestDto.isFetch_meta());
    }

    @Override
    public DefaultSuccessResponse processPatchInstitutionOperation(
            @NonNull final InstitutionRequestDto institutionRequestDto) {

        institutionValidator.validatePatchInstitutionRequest(institutionRequestDto);

        String institutionId = institutionRequestDto.getInstitutionData().id;
        Institution institution = institutionRepository.findById(institutionId).orElse(null);

        if (institution != null) {
            InstitutionMapper.INSTANCE.updatedInstitutionModel(institutionRequestDto, institution);
            if (institution.getInstitutionMeta() != null) {
                institution.getInstitutionMeta().setInstitution(institution);
            }
            institutionRepository.save(institution);
            return DefaultSuccessResponse.builder().isSuccess(true).build();
        }

        return DefaultSuccessResponse.builder().isSuccess(false).build();
    }

    @Override
    @Transactional
    public DefaultSuccessResponse processDeleteInstitutionOperation(
            @NonNull final String institutionId) {

        institutionValidator.validateDeleteInstitutionRequest(institutionId);

        Institution institution = institutionRepository.findById(institutionId).orElse(null);

        if (institution != null) {
            InstitutionMapper.INSTANCE.deleteInstitutionModel(institutionId, institution);
            Timestamp now = Util.getCurrentTimestamp();
            institution.setDeletedAt(now);
            if (institution.getInstitutionMeta() != null) {
                institution.getInstitutionMeta().setInstitution(institution);
                institution.getInstitutionMeta().setDeletedAt(now);
            }
            institutionRepository.save(institution);
            return DefaultSuccessResponse.builder().isSuccess(true).build();
        }
        return DefaultSuccessResponse.builder().isSuccess(false).build();
    }
}
