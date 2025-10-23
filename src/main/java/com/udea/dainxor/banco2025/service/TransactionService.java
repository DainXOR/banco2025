package com.udea.dainxor.banco2025.service;

import com.udea.dainxor.banco2025.dto.TransactionDTO;
import com.udea.dainxor.banco2025.dto.TransactionRequestDTO;
import com.udea.dainxor.banco2025.entity.Customer;
import com.udea.dainxor.banco2025.entity.Transaction;
import com.udea.dainxor.banco2025.mapper.TransactionMapper;
import com.udea.dainxor.banco2025.repository.CustomerRepository;
import com.udea.dainxor.banco2025.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionDTO create(TransactionRequestDTO transactionRequestDTO) {
        if(transactionRequestDTO.getSenderAccountNumber() == null || transactionRequestDTO.getReceiverAccountNumber() == null) {
            throw new IllegalArgumentException("Sender and receiver account numbers must be provided.");
        }

        Customer sender = customerRepository.findByAccountNumber(transactionRequestDTO.getSenderAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found."));

        Customer receiver = customerRepository.findByAccountNumber(transactionRequestDTO.getReceiverAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found."));

        if (sender.getBalance() < transactionRequestDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds in sender's account.");
        }

        sender.setBalance(sender.getBalance() - transactionRequestDTO.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionRequestDTO.getAmount());

        customerRepository.save(sender);
        customerRepository.save(receiver);

        Transaction transaction = transactionMapper.toEntity(transactionRequestDTO);
        transaction.setTimestamp(java.time.LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDTO(savedTransaction);
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    public List<TransactionDTO> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(accountNumber, accountNumber);
        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    public Optional<TransactionDTO> getTransactionById(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toDTO);
    }

}