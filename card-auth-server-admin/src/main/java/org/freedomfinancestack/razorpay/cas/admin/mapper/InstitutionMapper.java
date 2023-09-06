package org.freedomfinancestack.razorpay.cas.admin.mapper;

import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import lombok.NonNull;

@Mapper(imports = {InstitutionStatus.class})
public interface InstitutionMapper {

    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    @Mapping(target = "id", source = "institutionId")
    @Mapping(target = "name", source = "institutionName")
    @Mapping(target = "shortName", source = "institutionShortName")
    @Mapping(target = "isoCountryCode", source = "isoCountryCode")
    @Mapping(target = "timezone", source = "timezone")
    @Mapping(target = "createdBy", expression = "java(String.valueOf(\"dev-user\"))")
    @Mapping(target = "status", expression = "java(InstitutionStatus.ACTIVE)")
    Institution toInstitutionModel(@NonNull final InstitutionRequestDto institutionRequestDto);
}
