package org.ffs.razorpay.cas.acs.dto.mapper;

import org.ffs.razorpay.cas.acs.dto.CardDetailDto;
import org.ffs.razorpay.cas.dao.model.CardDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardDetailsMapper {
    final CardDetailsMapper INSTANCE = Mappers.getMapper(CardDetailsMapper.class);

    @Mapping(target = "cardholder.name", source = "name")
    @Mapping(target = "cardholder.mobileNumber", source = "mobileNumber")
    @Mapping(target = "cardholder.emailId", source = "emailId")
    @Mapping(target = "cardholder.dob", source = "dob")
    CardDetail toCardDetailModel(CardDetailDto cardDetailDto);

    @Mapping(target = "emailId", source = "cardholder.emailId")
    @Mapping(target = "dob", source = "cardholder.dob")
    @Mapping(target = "name", source = "cardholder.name")
    @Mapping(target = "mobileNumber", source = "cardholder.mobileNumber")
    CardDetailDto toCardDetailDto(CardDetail cardDetail);
}
