package org.freedomfinancestack.razorpay.cas.admin.mapper;

import org.freedomfinancestack.razorpay.cas.admin.dto.GetInstitutionResponseDto;
import org.freedomfinancestack.razorpay.cas.admin.dto.InstitutionRequestDto;
import org.freedomfinancestack.razorpay.cas.dao.enums.InstitutionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import lombok.NonNull;

@Mapper(
        imports = {InstitutionStatus.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InstitutionMapper {

    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "institutionData.id", defaultValue = ""),
        @Mapping(target = "name", source = "institutionData.institutionName"),
        @Mapping(target = "shortName", source = "institutionData.institutionShortName"),
        @Mapping(target = "isoCountryCode", source = "institutionData.isoCountryCode"),
        @Mapping(target = "timezone", source = "institutionData.timezone"),
        @Mapping(target = "messageVersion", source = "institutionData.messageVersion"),
        @Mapping(target = "createdBy", expression = "java(String.valueOf(\"dev-user\"))"),
        @Mapping(target = "modifiedBy", expression = "java(String.valueOf(\"dev-user\"))"),
        @Mapping(target = "status", expression = "java(InstitutionStatus.ACTIVE)"),
        @Mapping(target = "institutionMeta", source = "institutionRequestDto.institutionMetaData"),
        @Mapping(
                target = "institutionMeta.createdBy",
                expression = "java(String.valueOf(\"dev-user\"))"),
        @Mapping(
                target = "institutionMeta.modifiedBy",
                expression = "java(String.valueOf(\"dev-user\"))")
    })
    Institution toInstitutionModel(@NonNull final InstitutionRequestDto institutionRequestDto);

    @Mappings({
        @Mapping(source = "institution.id", target = "institutionData.id"),
        @Mapping(source = "institution.name", target = "institutionData.institutionName"),
        @Mapping(source = "institution.shortName", target = "institutionData.institutionShortName"),
        @Mapping(source = "institution.isoCountryCode", target = "institutionData.isoCountryCode"),
        @Mapping(source = "institution.timezone", target = "institutionData.timezone"),
        @Mapping(source = "institution.status", target = "institutionData.status"),
        @Mapping(source = "institution.messageVersion", target = "institutionData.messageVersion"),
        @Mapping(
                source = "institution.institutionMeta",
                target = "institutionMetaData",
                conditionExpression = "java(fetchMeta)",
                defaultExpression = "java(new InstitutionMetaData())")
    })
    GetInstitutionResponseDto toInstitutionDto(
            @NonNull final Institution institution, @NonNull final boolean fetchMeta);

    @Mappings({
        @Mapping(target = "id", source = "institutionData.id"),
        @Mapping(target = "name", source = "institutionData.institutionName"),
        @Mapping(target = "shortName", source = "institutionData.institutionShortName"),
        @Mapping(target = "isoCountryCode", source = "institutionData.isoCountryCode"),
        @Mapping(target = "timezone", source = "institutionData.timezone"),
        @Mapping(target = "messageVersion", source = "institutionData.messageVersion"),
        @Mapping(target = "modifiedBy", expression = "java(String.valueOf(\"dev-user-modified\"))"),
        @Mapping(target = "status", source = "institutionData.status"),
        @Mapping(target = "institutionMeta", source = "institutionRequestDto.institutionMetaData"),
        @Mapping(
                target = "institutionMeta.modifiedBy",
                expression = "java(String.valueOf(\"dev-user-modified\"))")
    })
    void updatedInstitutionModel(
            @NonNull InstitutionRequestDto institutionRequestDto,
            @NonNull @MappingTarget Institution institution);

    @Mappings({
        @Mapping(target = "id", source = "institutionId"),
        @Mapping(target = "status", expression = "java(InstitutionStatus.INACTIVE)"),
        @Mapping(target = "deletedBy", expression = "java(\"dev-user-delete\")"),
        @Mapping(
                target = "institutionMeta.deletedBy",
                expression = "java(String.valueOf(\"dev-user-delete\"))")
    })
    void deleteInstitutionModel(
            @NonNull String institutionId, @NonNull @MappingTarget Institution institution);

    default byte[] mapBase64StringToByteArray(String base64String) {
        if (base64String != null) {
            return java.util.Base64.getDecoder().decode(base64String);
        }
        return null;
    }

    default String mapByteArrayToBase64String(byte[] byteArray) {
        if (byteArray != null) {
            return java.util.Base64.getEncoder().encodeToString(byteArray);
        }
        return null;
    }
}
