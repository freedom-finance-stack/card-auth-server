package com.razorpay.threeds.dto.mapper;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    final TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDto toTransactionDto(Transaction transaction);
    Transaction toTransactionModel(TransactionDto transaction);
}
