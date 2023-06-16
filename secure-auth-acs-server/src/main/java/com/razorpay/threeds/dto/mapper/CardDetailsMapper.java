package com.razorpay.threeds.dto.mapper;

import com.razorpay.acs.dao.model.CardDetail;
import com.razorpay.threeds.dto.CardDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardDetailsMapper {

    final CardDetailsMapper INSTANCE = Mappers.getMapper(CardDetailsMapper.class);
    @Mapping(target="transactionReferenceDetail", source="transactionReferenceDetail")
    CardDetail toTransactionModel(CardDetailDto CardDetailDto);
}
