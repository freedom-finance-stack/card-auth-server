package org.freedomfinancestack.razorpay.cas.admin.mapper;

import org.freedomfinancestack.razorpay.cas.admin.dto.CreateInstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import lombok.NonNull;

@Mapper(imports = {InstitutionStatus.class})
public interface InstitutionMapper {

    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    @Mapping(target = "name", source = "institutionName")
    @Mapping(target = "shortName", source = "institutionShortName")
    @Mapping(target = "isoCountryCode", source = "isoCountryCode")
    @Mapping(target = "timezone", source = "timezone")
    @Mapping(target = "createdBy", expression = "java(String.valueOf(\"dev-user\"))")
    @Mapping(target = "modifiedBy", expression = "java(String.valueOf(\"dev-user\"))")
    @Mapping(target = "status", expression = "java(InstitutionStatus.ACTIVE)")
    @Mapping(target = "institutionMeta", source = "createInstitutionRequestDto.institutionMeta")
    @Mapping(
            target = "institutionMeta.createdBy",
            expression = "java(String.valueOf(\"dev-user\"))")
    @Mapping(
            target = "institutionMeta.modifiedBy",
            expression = "java(String.valueOf(\"dev-user\"))")
    Institution toInstitutionModel(
            @NonNull final CreateInstitutionRequestDto createInstitutionRequestDto);

    default byte[] mapBase64StringToByteArray(String base64String) {
        if (base64String != null) {
            return java.util.Base64.getDecoder().decode(base64String);
        }
        return null;
    }
}
