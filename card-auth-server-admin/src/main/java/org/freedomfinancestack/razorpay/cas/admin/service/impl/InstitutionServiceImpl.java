package org.freedomfinancestack.razorpay.cas.admin.service.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.razorpay.cas.admin.dto.*;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.GetInstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.GetInstitutionResponseDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.InstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.exception.GlobalExceptionHandler;
import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.ACSAdminDataAccessException;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.mapper.InstitutionMapper;
import org.freedomfinancestack.razorpay.cas.admin.module.configuration.MetaDataConfiguration;
import org.freedomfinancestack.razorpay.cas.admin.service.InstitutionService;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.InstitutionValidator;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("InstitutionServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstitutionServiceImpl implements InstitutionService {

    @Autowired MetaDataConfiguration metaDataConfiguration;

    private final InstitutionValidator institutionValidator;

    private final InstitutionRepository institutionRepository;

    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    @Transactional
    public ResponseEntity<?> processCreateInstitutionOperation(
            @NonNull final InstitutionRequestDto institutionRequestDto) {

        try {
            institutionValidator.validateCreateInstitutionRequest(institutionRequestDto);

            Institution institution =
                    InstitutionMapper.INSTANCE.toInstitutionModel(institutionRequestDto);

            String institutionId = Util.generateUUID();

            institution.setId(institutionId);

            if (institution.getInstitutionMeta() != null) {
                institution.getInstitutionMeta().setInstitution(institution);
            }

            saveOrUpdate(institution);

            return new ResponseEntity<>(
                    DefaultSuccessResponse.builder().isSuccess(true).build(), HttpStatus.OK);
        } catch (ValidationException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getValidationErrorCode().getErrorCode());
            return globalExceptionHandler.handleRequestValidationException(
                    new RequestValidationException(ex));
        } catch (ACSAdminDataAccessException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getErrorCode().getCode());

            return globalExceptionHandler.handleACSAdminDataAccessException(ex);

        } catch (Exception ex) {
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            return globalExceptionHandler.handleException(ex);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> processGetInstitutionOperation(
            @NonNull final GetInstitutionRequestDto getInstitutionRequestDto) {

        try {
            institutionValidator.validateGetInstitutionRequest(getInstitutionRequestDto);

            Institution institution = findById(getInstitutionRequestDto.getInstitution_id());

            return new ResponseEntity<>(
                    InstitutionMapper.INSTANCE.toInstitutionDto(
                            institution, getInstitutionRequestDto.isFetch_meta()),
                    HttpStatus.OK);

        } catch (ValidationException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getValidationErrorCode().getErrorCode());
            return globalExceptionHandler.handleRequestValidationException(
                    new RequestValidationException(ex));
        } catch (ACSAdminDataAccessException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getErrorCode().getCode());
            return globalExceptionHandler.handleACSAdminDataAccessException(ex);
        } catch (DataNotFoundException ex) {
            log.warn(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getErrorCode().getCode());
            return new ResponseEntity<>(new GetInstitutionResponseDto(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            return globalExceptionHandler.handleException(ex);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> processPatchInstitutionOperation(
            @NonNull final InstitutionRequestDto institutionRequestDto) {

        try {
            institutionValidator.validatePatchInstitutionRequest(institutionRequestDto);

            String institutionId = institutionRequestDto.getInstitutionData().id;
            Institution institution = findById(institutionId);

            if (institution != null) {
                InstitutionMapper.INSTANCE.updatedInstitutionModel(
                        institutionRequestDto, institution);
                if (institution.getInstitutionMeta() != null) {
                    institution.getInstitutionMeta().setInstitution(institution);
                }
                saveOrUpdate(institution);
            }
        } catch (ValidationException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getValidationErrorCode().getErrorCode());
            return globalExceptionHandler.handleRequestValidationException(
                    new RequestValidationException(ex));
        } catch (ACSAdminDataAccessException | DataNotFoundException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getErrorCode().getCode());
            if (ex instanceof ACSAdminDataAccessException) {
                return globalExceptionHandler.handleACSAdminDataAccessException(
                        (ACSAdminDataAccessException) ex);
            } else {
                return globalExceptionHandler.handleDataNotFoundException(
                        (DataNotFoundException) ex);
            }
        } catch (Exception ex) {
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            return globalExceptionHandler.handleException(ex);
        }
        return new ResponseEntity<>(
                DefaultSuccessResponse.builder().isSuccess(true).build(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> processDeleteInstitutionOperation(
            @NonNull final String institutionId) {

        try {
            institutionValidator.validateDeleteInstitutionRequest(institutionId);

            Institution institution = findById(institutionId);

            if (institution != null) {
                InstitutionMapper.INSTANCE.deleteInstitutionModel(institutionId, institution);
                Timestamp now = Util.getCurrentTimestamp();
                institution.setDeletedAt(now);
                if (institution.getInstitutionMeta() != null) {
                    institution.getInstitutionMeta().setInstitution(institution);
                    institution.getInstitutionMeta().setDeletedAt(now);
                }
                saveOrUpdate(institution);
            }
        } catch (ValidationException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getValidationErrorCode().getErrorCode());
            return globalExceptionHandler.handleRequestValidationException(
                    new RequestValidationException(ex));
        } catch (ACSAdminDataAccessException | DataNotFoundException ex) {
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getErrorCode().getCode());
            if (ex instanceof ACSAdminDataAccessException) {
                return globalExceptionHandler.handleACSAdminDataAccessException(
                        (ACSAdminDataAccessException) ex);
            } else {
                return globalExceptionHandler.handleDataNotFoundException(
                        (DataNotFoundException) ex);
            }
        } catch (Exception ex) {
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            return globalExceptionHandler.handleException(ex);
        }
        return new ResponseEntity<>(
                DefaultSuccessResponse.builder().isSuccess(true).build(), HttpStatus.OK);
    }

    private void saveOrUpdate(Institution institution) throws ACSAdminDataAccessException {
        try {
            institutionRepository.save(institution);
        } catch (DataAccessException ex) {
            log.error("Error while saving Institution", ex);
            throw new ACSAdminDataAccessException(InternalErrorCode.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private Institution findById(String id)
            throws ACSAdminDataAccessException, DataNotFoundException {
        if (Util.isNullorBlank(id)) {
            return null;
        }
        try {
            Optional<Institution> institution = institutionRepository.findById(id);
            if (institution.isPresent()) {
                return institution.get();
            }
        } catch (DataAccessException ex) {
            log.error("Error while fetching institution for given ID: " + id);
            throw new ACSAdminDataAccessException(InternalErrorCode.INTERNAL_SERVER_ERROR, ex);
        }
        log.error("Institution not found for ID: " + id);
        throw new DataNotFoundException(InternalErrorCode.DATA_NOT_FOUND);
    }
}
