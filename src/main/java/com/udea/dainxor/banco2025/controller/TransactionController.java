package com.udea.dainxor.banco2025.controller;

import com.udea.dainxor.banco2025.DTO.TransactionDTO;
import com.udea.dainxor.banco2025.DTO.TransactionRequestDTO;
import com.udea.dainxor.banco2025.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        try {
            TransactionDTO result = transactionService.create(transactionRequestDTO);
            return ResponseEntity.status(201).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionsByID(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }
}
