package com.udea.dainxor.banco2025.mapper;

import com.udea.dainxor.banco2025.DTO.CustomerDTO;
import com.udea.dainxor.banco2025.entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-03T22:59:18-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDTO(Customer costumer) {
        if ( costumer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId( costumer.getId() );
        customerDTO.setAccountNumber( costumer.getAccountNumber() );
        customerDTO.setFirstName( costumer.getFirstName() );
        customerDTO.setLastName( costumer.getLastName() );
        customerDTO.setBalance( costumer.getBalance() );

        return customerDTO;
    }

    @Override
    public Customer toEntity(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( customerDTO.getId() );
        customer.setAccountNumber( customerDTO.getAccountNumber() );
        customer.setFirstName( customerDTO.getFirstName() );
        customer.setLastName( customerDTO.getLastName() );
        customer.setBalance( customerDTO.getBalance() );

        return customer;
    }
}
