package org.freedomfinancestack.razorpay.cas.admin.validation;

import org.freedomfinancestack.extensions.validation.enums.DataLengthType;
import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.GetInstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.InstitutionData;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.InstitutionMetaData;
import org.freedomfinancestack.razorpay.cas.admin.dto.institution.InstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.admin.module.configuration.MetaDataConfiguration;
import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.extensions.validation.validator.basic.IsUUID.isUUID;
import static org.freedomfinancestack.extensions.validation.validator.basic.NotBlank.notBlank;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsBase64.isBase64;
import static org.freedomfinancestack.extensions.validation.validator.enriched.IsIn.isIn;
import static org.freedomfinancestack.extensions.validation.validator.enriched.LengthValidator.lengthValidator;
import static org.freedomfinancestack.extensions.validation.validator.rule.When.when;

@Slf4j
@Component(value = "institutionValidator")
public class InstitutionValidator {

    @Autowired MetaDataConfiguration metaDataConfiguration;

    public void validateCreateInstitutionRequest(
            @NonNull final InstitutionRequestDto institutionRequestDto) throws ValidationException {
        validateCreateRequestInstitutionData(institutionRequestDto.getInstitutionData());
        validateCreateRequestInstitutionMetaData(institutionRequestDto.getInstitutionMetaData());
    }

    private void validateCreateRequestInstitutionData(InstitutionData institutionData)
            throws ValidationException {
        Validation.validate(
                "institutionName",
                institutionData.institutionName,
                notBlank(),
                lengthValidator(DataLengthType.VARIABLE, 100));
        Validation.validate(
                "institutionShortName",
                institutionData.institutionShortName,
                notBlank(),
                lengthValidator(DataLengthType.VARIABLE, 20));
        Validation.validate(
                "isoCountryCode",
                institutionData.isoCountryCode,
                notBlank(),
                isIn(metaDataConfiguration.getIsoCountryCode()));
        Validation.validate(
                "messageVersion",
                institutionData.messageVersion,
                notBlank(),
                isIn(metaDataConfiguration.getSupportedMessageVersions()));
        Validation.validate(
                "timezone",
                institutionData.timezone,
                notBlank(),
                isIn(metaDataConfiguration.getSupportedTimezone()));
    }

    private void validateCreateRequestInstitutionMetaData(InstitutionMetaData institutionMetaData)
            throws ValidationException {
        Validation.validate("logoData", institutionMetaData.getLogoData(), notBlank(), isBase64());
        Validation.validate(
                "logoFileName",
                institutionMetaData.getLogoFilename(),
                notBlank(),
                lengthValidator(DataLengthType.VARIABLE, 50));
    }

    public void validateGetInstitutionRequest(
            @NonNull final GetInstitutionRequestDto getInstitutionRequestDto)
            throws ValidationException {
        Validation.validate(
                "institution_id",
                getInstitutionRequestDto.getInstitution_id(),
                notBlank(),
                isUUID());
        Validation.validate("", getInstitutionRequestDto.isFetch_meta(), notBlank());
    }

    public void validatePatchInstitutionRequest(
            @NonNull final InstitutionRequestDto institutionRequestDto) throws ValidationException {
        validateUpdateRequestInstitutionData(institutionRequestDto.getInstitutionData());
        validateUpdateRequestInstitutionMetaData(institutionRequestDto.getInstitutionMetaData());
    }

    private void validateUpdateRequestInstitutionData(InstitutionData institutionData)
            throws ValidationException {
        Validation.validate("id", institutionData.id, notBlank(), isUUID());
        Validation.validate(
                "institutionName",
                institutionData.institutionName,
                when(
                        (institutionData.institutionName != null),
                        lengthValidator(DataLengthType.VARIABLE, 100)));
        Validation.validate(
                "institutionShortName",
                institutionData.institutionShortName,
                when(
                        (institutionData.institutionShortName != null),
                        lengthValidator(DataLengthType.VARIABLE, 20)));
        Validation.validate(
                "isoCountryCode",
                institutionData.isoCountryCode,
                when(
                        (institutionData.isoCountryCode != null),
                        isIn(metaDataConfiguration.getIsoCountryCode())));
        Validation.validate(
                "messageVersion",
                institutionData.messageVersion,
                when(
                        (institutionData.messageVersion != null),
                        isIn(metaDataConfiguration.getSupportedMessageVersions())));
        Validation.validate(
                "timezone",
                institutionData.timezone,
                when(
                        (institutionData.timezone != null),
                        isIn(metaDataConfiguration.getSupportedTimezone())));
        Validation.validate(
                "status",
                institutionData.status,
                when(
                        (institutionData.status != null),
                        isIn(
                                new String[] {
                                    InstitutionStatus.ACTIVE.name(),
                                    InstitutionStatus.INACTIVE.name()
                                })));
    }

    private void validateUpdateRequestInstitutionMetaData(InstitutionMetaData institutionMetaData)
            throws ValidationException {
        Validation.validate("institutionMetaData", institutionMetaData, notBlank());
        Validation.validate(
                "logoData",
                institutionMetaData.getLogoData(),
                when((institutionMetaData.getLogoData() != null), isBase64()));
        Validation.validate(
                "logoFileName",
                institutionMetaData.getLogoFilename(),
                when(
                        (institutionMetaData.getLogoFilename() != null),
                        lengthValidator(DataLengthType.VARIABLE, 50)));
    }

    public void validateDeleteInstitutionRequest(@NonNull final String institutionId)
            throws ValidationException {
        Validation.validate("institution_id", institutionId, notBlank(), isUUID());
    }
}
