package com.udea.dainxor.banco2025.controller;

import com.udea.dainxor.banco2025.DTO.CustomerDTO;
import com.udea.dainxor.banco2025.DTO.DepositDTO;
import com.udea.dainxor.banco2025.service.CustomerService;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        System.out.println("Received id: " + id);
        return customerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<CustomerDTO> getByAccountNumber(@PathVariable String accountNumber) {
        System.out.println("Received number: " + accountNumber);
        return customerService.getByAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        // print the received customerDTO to the console
        System.out.println("Received CustomerDTO:");
        System.out.println("- " + customerDTO.getFirstName());
        System.out.println("- " + customerDTO.getLastName());

        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @PostMapping("/deposit")
    public ResponseEntity<CustomerDTO> depositMoney(@RequestBody DepositDTO depositDTO){

        return ResponseEntity.ok(customerService.deposit(depositDTO.getId(), depositDTO.getAmount()));
    }
}
