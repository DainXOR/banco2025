package com.udea.dainxor.banco2025.mapper;

import com.udea.dainxor.banco2025.DTO.TransactionDTO;
import com.udea.dainxor.banco2025.DTO.TransactionRequestDTO;
import com.udea.dainxor.banco2025.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);
    Transaction toEntity(TransactionRequestDTO transactionRequestDTO);
}
