package com.udea.dainxor.banco2025.controller;

import com.udea.dainxor.banco2025.DTO.TransactionDTO;
import com.udea.dainxor.banco2025.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO result = transactionService.transfer(transactionDTO);
            return ResponseEntity.status(201).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{accountNumber}")
    public List<TransactionDTO> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return transactionService.getTransactionsByAccountNumber(accountNumber);
    }
}
