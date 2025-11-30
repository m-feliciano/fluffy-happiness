package br.com.microservices.customer.domain.mapper;

import br.com.microservices.customer.domain.model.Customer;
import br.com.microservices.customer.domain.request.CustomerRequest;
import br.com.microservices.customer.domain.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    Customer toCustomer(CustomerRequest request);
}