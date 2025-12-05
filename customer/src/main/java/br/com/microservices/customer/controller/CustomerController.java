package br.com.microservices.customer.controller;

import br.com.microservices.customer.controller.openapi.CustomerControllerOpenApi;
import br.com.microservices.customer.domain.request.CustomerRequest;
import br.com.microservices.customer.domain.response.CustomerResponse;
import br.com.microservices.customer.service.ICustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController implements CustomerControllerOpenApi {

    private final ICustomerService customerService;

    @PostMapping
    @Override
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER', 'ADMIN')")
    public ResponseEntity<CustomerResponse> register(
            @RequestBody @Validated CustomerRequest customerRequest) {
        log.info("New customer registration {}", customerRequest);
        CustomerResponse response = customerService.save(customerRequest);
        log.info("Customer registered successfully");
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    @Override
    @PreAuthorize("hasAnyRole('USER', 'OPERATOR', 'MANAGER', 'ADMIN')")
    public Page<CustomerResponse> findAll(@PageableDefault Pageable pageRequest) {
        log.info("Finding all customers");
        return customerService.findAll(pageRequest);
    }
}
