package com.udea.dainxor.banco2025.service;

import com.udea.dainxor.banco2025.DTO.CustomerDTO;
import com.udea.dainxor.banco2025.entity.Customer;
import com.udea.dainxor.banco2025.mapper.CustomerMapper;
import com.udea.dainxor.banco2025.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::toDTO)
                .toList();
    }

    public Optional<CustomerDTO> getById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDTO);
    }

    public Optional<CustomerDTO> getByAccountNumber(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber).map(customerMapper::toDTO);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);

        if (customer.getId() != null) {
            customer.setId(null); // Ensure ID is null to auto-generate
        }
        if (customer.getAccountNumber() != null) {
            customer.setAccountNumber(null); // Ensure account number is null to auto-generate
        }

        customer.setBalance(0.0);
        long newAccountNumber = customerRepository.count() + 1;
        customer.setAccountNumber(Long.toString(newAccountNumber));

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO deposit(Long id, double amount){
        Customer customer = customerRepository.findById(id)
                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (amount < 0.0){
            throw new IllegalArgumentException("The amount for deposit must be positive.");
        }

        customer.setBalance(customer.getBalance() + amount);
        Customer savedEntity = customerRepository.save(customer);
        return customerMapper.toDTO(savedEntity);
    }

}
