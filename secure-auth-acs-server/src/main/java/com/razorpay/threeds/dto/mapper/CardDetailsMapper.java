package com.razorpay.threeds.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.razorpay.acs.dao.model.CardDetail;
import com.razorpay.threeds.dto.CardDetailDto;

@Mapper
public interface CardDetailsMapper {

  final CardDetailsMapper INSTANCE = Mappers.getMapper(CardDetailsMapper.class);

  @Mapping(target = "cardNumber", source = "cardNumber")
  CardDetail toCardDetailModel(CardDetailDto CardDetailDto);
}
