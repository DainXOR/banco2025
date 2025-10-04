package com.udea.dainxor.banco2025.mapper;

import com.udea.dainxor.banco2025.DTO.TransactionDTO;
import com.udea.dainxor.banco2025.DTO.TransactionRequestDTO;
import com.udea.dainxor.banco2025.entity.Transaction;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-03T22:59:19-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionDTO toDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setId( transaction.getId() );
        transactionDTO.setSenderAccountNumber( transaction.getSenderAccountNumber() );
        transactionDTO.setReceiverAccountNumber( transaction.getReceiverAccountNumber() );
        transactionDTO.setAmount( transaction.getAmount() );
        transactionDTO.setTimestamp( transaction.getTimestamp() );

        return transactionDTO;
    }

    @Override
    public Transaction toEntity(TransactionDTO transactionDTO) {
        if ( transactionDTO == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setId( transactionDTO.getId() );
        transaction.setSenderAccountNumber( transactionDTO.getSenderAccountNumber() );
        transaction.setReceiverAccountNumber( transactionDTO.getReceiverAccountNumber() );
        transaction.setAmount( transactionDTO.getAmount() );
        transaction.setTimestamp( transactionDTO.getTimestamp() );

        return transaction;
    }

    @Override
    public Transaction toEntity(TransactionRequestDTO transactionRequestDTO) {
        if ( transactionRequestDTO == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setSenderAccountNumber( transactionRequestDTO.getSenderAccountNumber() );
        transaction.setReceiverAccountNumber( transactionRequestDTO.getReceiverAccountNumber() );
        transaction.setAmount( transactionRequestDTO.getAmount() );

        return transaction;
    }
}
