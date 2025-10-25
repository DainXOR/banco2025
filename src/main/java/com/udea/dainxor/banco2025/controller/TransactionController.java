package com.udea.dainxor.banco2025.controller;

import com.udea.dainxor.banco2025.types.HttpResponse;
import com.udea.dainxor.banco2025.dto.TransactionDTO;
import com.udea.dainxor.banco2025.dto.TransactionRequestDTO;
import com.udea.dainxor.banco2025.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public HttpResponse<TransactionDTO> create(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        var result = transactionService.create(transactionRequestDTO);

        return HttpResponse.fromResult(result, HttpStatus.CREATED, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public HttpResponse<TransactionDTO> getTransactionByID(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map((transaction) -> HttpResponse.success(transaction, HttpStatus.OK))
                .orElseGet(() -> HttpResponse.error("Transaction not found", HttpStatus.NOT_FOUND));

    }
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }
}
