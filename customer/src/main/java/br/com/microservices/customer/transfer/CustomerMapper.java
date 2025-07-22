package br.com.microservices.customer.transfer;

import br.com.microservices.customer.domain.model.Customer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomerMapper {

    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerDto.builder()
                .customerId(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .email(customer.getEmail())
                .build();
    }

    public static Customer fromDto(CustomerDto customerDto) {
        if (customerDto == null) {
            return null;
        }

        return Customer.builder()
                .id(customerDto.customerId())
                .firstname(customerDto.firstname())
                .lastname(customerDto.lastname())
                .email(customerDto.email())
                .build();
    }
}