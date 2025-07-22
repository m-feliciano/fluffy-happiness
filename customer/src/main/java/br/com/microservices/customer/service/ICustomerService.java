package br.com.microservices.customer.service;

import br.com.microservices.customer.domain.model.Customer;
import br.com.microservices.customer.transfer.CustomerDto;

import java.util.List;

public interface ICustomerService {

    void save(CustomerDto dto);

    boolean isEmailTaken(String email);

    List<Customer> findAll();
}
