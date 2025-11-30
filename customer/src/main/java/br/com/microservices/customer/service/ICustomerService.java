package br.com.microservices.customer.service;

import br.com.microservices.customer.domain.request.CustomerRequest;
import br.com.microservices.customer.domain.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {

    CustomerResponse save(CustomerRequest request);

    boolean isEmailTaken(String email);

    Page<CustomerResponse> findAll(Pageable pageable);
}
