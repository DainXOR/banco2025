package com.udea.dainxor.banco2025.mapper;

import com.udea.dainxor.banco2025.DTO.CustomerDTO;
import com.udea.dainxor.banco2025.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper Instance = Mappers.getMapper(CustomerMapper.class);
    CustomerDTO toDTO(Customer costumer);
    Customer toEntity(CustomerDTO customerDTO);
}
