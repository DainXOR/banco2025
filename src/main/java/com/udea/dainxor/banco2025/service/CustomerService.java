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

    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDTO);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
    }
}
